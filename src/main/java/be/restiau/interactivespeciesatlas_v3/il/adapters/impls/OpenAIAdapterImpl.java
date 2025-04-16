package be.restiau.interactivespeciesatlas_v3.il.adapters.impls;

import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsGbifDTO;
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
    public Mono<SpeciesDetailsEnriched> getSpeciesDetailsEnriched(SpeciesDetailsGbifDTO dto) {
        String prompt = """
                Tu es un naturaliste expert. Décris l'espèce suivante en 255 caractères max. 
                Commence par le nom vernaculaire. Donne son habitat, alimentation, comportement social et une particularité.
                
                Espèce : %s (%s)
                """.formatted(dto.vernacularName(), dto.scientificName());

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
                    String[] segments = description.split("\n\n");

                    return new SpeciesDetailsEnriched(
                            dto.gbifId(),
                            dto.scientificName(),
                            dto.vernacularName(),
                            dto.canonicalName(),
                            dto.kingdom(),
                            dto.phylum(),
                            dto.order(),
                            dto.family(),
                            dto.genus(),
                            dto.species(),
                            dto.imageUrl(),
                            List.of(segments),
                            dto.coords()
                    );
                })
                .onErrorResume(error -> {
                    log.error("Erreur lors de l'appel à OpenAI : {}", error.getMessage());

                    // fallback avec une description générique
                    String fallbackDescription = "Description non disponible pour le moment.";

                    return Mono.just(new SpeciesDetailsEnriched(
                            dto.gbifId(),
                            dto.scientificName(),
                            dto.vernacularName(),
                            dto.canonicalName(),
                            dto.kingdom(),
                            dto.phylum(),
                            dto.order(),
                            dto.family(),
                            dto.genus(),
                            dto.species(),
                            dto.imageUrl(),
                            List.of(fallbackDescription),
                            dto.coords()
                    ));
                });
    }
}
