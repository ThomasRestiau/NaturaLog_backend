package be.restiau.interactivesatlasspecies_v3.il.adapters;

import reactor.core.publisher.Mono;

import java.util.List;

public interface OpenAIAdapter {
    Mono<List<String>> getDescription(String scientificName, String vernacularName);
    Mono<String> getResponse(String userMessage);
}
