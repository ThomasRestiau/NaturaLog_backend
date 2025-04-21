package be.restiau.interactivespeciesatlas_v3.il.adapters;

import reactor.core.publisher.Mono;

import java.util.List;

public interface OpenAIAdapter {
    Mono<List<String>> getDescription(String scientificName, String vernacularName);
}
