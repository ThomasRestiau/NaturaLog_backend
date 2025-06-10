package be.restiau.interactivesatlasspecies_v3.il.adapters.impls;

import be.restiau.interactivesatlasspecies_v3.api.models.species.dto.SpeciesCoordDTO;
import be.restiau.interactivesatlasspecies_v3.bll.exceptions.llm.LlmResponseException;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsGbifDTO;
import be.restiau.interactivesatlasspecies_v3.il.adapters.LlmAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LlmAdapterImpl implements LlmAdapter {

    private final WebClient llamaWebClient;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Cacheable("description")
    @Override
    public Mono<SpeciesDetailsEnriched> getSpeciesDetailsEnriched(SpeciesDetailsGbifDTO speciesDetailsGbifDTO) {
        String gbifId = speciesDetailsGbifDTO.gbifId();
        String scientificName = speciesDetailsGbifDTO.scientificName();
        String vernacularName = speciesDetailsGbifDTO.vernacularName();
        String canonicalName = speciesDetailsGbifDTO.canonicalName();
        String kingdom = speciesDetailsGbifDTO.kingdom();
        String phylum = speciesDetailsGbifDTO.phylum();
        String order = speciesDetailsGbifDTO.order();
        String family = speciesDetailsGbifDTO.family();
        String genus = speciesDetailsGbifDTO.genus();
        String species = speciesDetailsGbifDTO.species();
        String imageUrl = speciesDetailsGbifDTO.imageUrl();
        List<SpeciesCoordDTO> coords = speciesDetailsGbifDTO.coords();

        String prompt = """
                Tu es un naturaliste. Rédige une **courte description vulgarisée** d'une espèce animale, en français.
                
                Contraintes :
                - **255 caractères maximum**
                - Le texte doit être une **phrase unique fluide** (pas de puces, pas de titre, pas de liste)
                - Commence par le **nom vernaculaire** si disponible, sinon le nom canonique
                - Inclue si possible : habitat, alimentation, comportement ou fait unique
                - Ne répète pas le nom de l'espèce
                - Ne fais pas de généralités vagues ni de texte promotionnel
                - Sois clair, naturel, et accessible à tout public
                
                Données disponibles :
                - Règne : %s
                - Embranchement : %s
                - Ordre : %s
                - Famille : %s
                - Genre : %s
                - Espèce : %s
                - Nom scientifique : %s
                - Nom vernaculaire : %s
                - Nom canonique : %s
                """.formatted(kingdom, phylum, order, family, genus, species, scientificName, vernacularName, canonicalName);


        return llamaWebClient.post()
                .uri("/v1/completions")
                .bodyValue(Map.of(
                        "prompt", prompt,
                        "n_predict", 300
                ))
                .retrieve()
                .bodyToMono(String.class)
                .map(descriptionResponse -> new SpeciesDetailsEnriched(
                        gbifId,
                        scientificName,
                        vernacularName,
                        canonicalName,
                        kingdom,
                        phylum,
                        order,
                        family,
                        genus,
                        species,
                        imageUrl,
                        List.of(cleanDescription(descriptionResponse)),
                        coords
                ));

    }

    private String cleanDescription(String raw) {
        if (raw == null || raw.isEmpty()) return "";

        try {
            JsonNode root = mapper.readTree(raw);

            // Cas OpenAI-style
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty() && choices.get(0).has("text")) {
                return choices.get(0).get("text").asText().trim().replace("\n", " ").replace("\n\n", " ");
            }

            // Cas "content"
            if (root.has("content")) {
                return root.get("content").asText().trim();
            }

            // Cas où la description est directement dans un champ de haut niveau
            return root.toString();
        } catch (JsonProcessingException e) {
            log.error("Error while parsing LLM response: {}", raw);
            throw new LlmResponseException(HttpStatus.BAD_GATEWAY, "Error parsing LLM response");
        }
    }

}
