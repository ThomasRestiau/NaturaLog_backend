package be.restiau.interactivesatlasspecies_v3.il.adapters;

import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsGbifDTO;
import reactor.core.publisher.Mono;

public interface LlmAdapter {
    Mono<SpeciesDetailsEnriched> getSpeciesDetailsEnriched(SpeciesDetailsGbifDTO speciesDetailsGbifDTO);
}
