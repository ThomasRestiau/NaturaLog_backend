package be.restiau.naturalog_backend.il.adapters.impls;

import be.restiau.naturalog_backend.il.adapters.OpenAIAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIAdapterImpl implements OpenAIAdapter {

    private final WebClient openAiWebClient;

    @Cacheable("description")
    public Mono<List<String>> getDescription(String scientificName, String vernacularName) {
        String prompt = """
                Tu es un naturaliste expert. Décris l'espèce suivante en 255 caractères max.
                Commence par le nom vernaculaire. Donne son habitat, alimentation, comportement social et une particularité.
                Pour chaque élément (nom vernaculaire, habitat, alimentation, comportement social et particularité),
                termine par deux sauts de ligne (\n\n).
                
                Espèce : %s (%s)""".formatted(vernacularName, scientificName);


        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "Tu es un naturaliste expert."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 200,
                "temperature", 0.7
        );

        return openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .timeout(Duration.ofSeconds(5)) // ⏱️ on attend max 5 sec
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(3))
                                .filter(error -> {
                                    if (error instanceof WebClientResponseException wcre) {
                                        int status = wcre.getStatusCode().value();
                                        return status == 429 || (status >= 500 && status < 600); // Retry sur 429 + erreurs serveur
                                    }
                                    return false;
                                })
                                .onRetryExhaustedThrow((retrySpec, signal) -> signal.failure())
                )
                .map(json -> {
                    String description = json.get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText();
                    String[] segments = description.split("(?<=\\n\\n)(?=\\S)");
                    return List.of(segments);
                })
                .onErrorResume(error -> {
                    log.error("Erreur lors de l'appel à OpenAI : {}", error.getMessage());
                    return Mono.just(List.of("Description non disponible pour le moment."));
                });
    }

    @Override
    public Mono<String> getResponse(String userMessage) {

        String prompt = """
                Tu es un assistant naturaliste expert dans les espèces animales et végétales du monde entier.
                Tes réponses doivent être simples, pédagogiques et adaptées à un public curieux.
                Lorsque l'utilisateur pose une question, donne une réponse claire, factuelle et si possible avec des anecdotes intéressantes ou des faits insolites.
                Si tu ne connais pas la réponse, propose une piste de recherche.
                Reste toujours bienveillant et encourageant.
                Si une question sort de ce contexte, tu réponds : "Je suis un chatbot Naturaliste, je ne réponds qu'aux questions sur la nature."
                La réponse est de maximum 200 caractères.
                Voici la question de l'utilisateur : %s
                """.formatted(userMessage);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "Tu es un naturaliste expert."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 200,
                "temperature", 0.7
        );

        return openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                        return json.get("choices")
                                .get(0)
                                .get("message")
                                .get("content")
                                .asText();
                })
                .onErrorResume(error -> {
                    log.error("Erreur dans getResponse : {}", error.getMessage());
                    return Mono.just("Je n’ai pas pu répondre pour le moment. N'hésite pas à réessayer !");
                });
    }
}
