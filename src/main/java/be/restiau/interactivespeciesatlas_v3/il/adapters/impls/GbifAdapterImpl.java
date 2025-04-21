package be.restiau.interactivespeciesatlas_v3.il.adapters.impls;

import be.restiau.interactivespeciesatlas_v3.api.models.species.dto.SpeciesCoordDTO;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsGbifDTO;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivespeciesatlas_v3.il.adapters.GbifAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GbifAdapterImpl implements GbifAdapter {

    private final WebClient.Builder webClientBuilder;

    private static final String GBIF_BASE_URL = "https://api.gbif.org/v1";

    @Override
    public List<SpeciesShortGbifDTO> searchByVernacularName(String name) {
        WebClient webClient = webClientBuilder.baseUrl(GBIF_BASE_URL).build();

        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/species/search")
                        .queryParam("q", name)
                        .queryParam("type", "VERNACULAR")
                        .queryParam("lang", "fr")
                        .queryParam("limit", 50)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null || !response.has("results")) {
            return Collections.emptyList();
        }

        List<SpeciesShortGbifDTO> results = new ArrayList<>();
        Set<String> seenScientificNames = new HashSet<>();
        Set<String> seenVernacularNames = new HashSet<>();

        for (JsonNode node : response.get("results")) {
            String key = node.has("key") ? node.get("key").asText() : null;
            String scientificName = node.has("scientificName") ? node.get("scientificName").asText() : null;

            if (scientificName == null || seenScientificNames.contains(scientificName)) {
                continue;
            }

            JsonNode detailedResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/species/" + key)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (detailedResponse == null) continue;

            String vernacularName = detailedResponse.has("vernacularName") ? detailedResponse.get("vernacularName").asText() : null;
            String canonicalName = detailedResponse.has("canonicalName") ? detailedResponse.get("canonicalName").asText() : null;

            if(seenVernacularNames.contains(vernacularName)){
                continue;
            }

            results.add(new SpeciesShortGbifDTO(key, scientificName, vernacularName, canonicalName));
            seenScientificNames.add(scientificName);
            seenVernacularNames.add(vernacularName);
        }

        return results;
    }

    @Override
    public Mono<SpeciesDetailsGbifDTO> getSpeciesDetailsGbifDTO(String key) {
        WebClient webClient = webClientBuilder.baseUrl(GBIF_BASE_URL).build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/species/" + key)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(speciesDetails -> {
                    String scientificName = getText(speciesDetails, "scientificName");
                    String vernacularName = getText(speciesDetails, "vernacularName");
                    String canonicalName = getText(speciesDetails, "canonicalName");
                    String kingdom = getText(speciesDetails, "kingdom");
                    String phylum = getText(speciesDetails, "phylum");
                    String order = getText(speciesDetails, "order");
                    String family = getText(speciesDetails, "family");
                    String genus = getText(speciesDetails, "genus");
                    String species = getText(speciesDetails, "species");

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/occurrence/search")
                                    .queryParam("scientificName", scientificName)
                                    .queryParam("hasCoordinate", true)
                                    .queryParam("limit", 300)
                                    .build())
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .map(mediaResponse -> {
                                JsonNode results = mediaResponse.get("results");
                                String imageUrl = null;

                                if (results != null && results.isArray() && !results.isEmpty()) {
                                    JsonNode firstResult = results.get(0);
                                    if (firstResult.has("media") && firstResult.get("media").isArray() && !firstResult.get("media").isEmpty()) {
                                        imageUrl = firstResult.get("media").get(0).get("identifier").asText();
                                    }
                                }

                                List<SpeciesCoordDTO> coords = new ArrayList<>();


                                assert results != null;
                                results.forEach(result -> {
                                    String lat = result.get("decimalLatitude").asText();
                                    String lng = result.get("decimalLongitude").asText();
                                    String country = result.get("country").asText();
                                    coords.add(new SpeciesCoordDTO(lat, lng, country));
                                });

                                return new SpeciesDetailsGbifDTO(
                                        key,
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
                                        coords
                                );
                            });
                });

    }

    private String getText(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asText() : null;
    }

}
