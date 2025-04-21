package be.restiau.interactivespeciesatlas_v3.il.adapters.impls;

import be.restiau.interactivespeciesatlas_v3.il.adapters.OpenAIAdapter;
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
                
                Espèce : %s (%s)\n\n""".formatted(vernacularName, scientificName);


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
}
