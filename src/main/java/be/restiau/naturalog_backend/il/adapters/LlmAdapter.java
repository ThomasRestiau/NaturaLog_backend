package be.restiau.naturalog_backend.il.adapters;

import be.restiau.naturalog_backend.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.naturalog_backend.bll.models.dto.SpeciesDetailsGbifDTO;
import reactor.core.publisher.Mono;

public interface LlmAdapter {
    Mono<SpeciesDetailsEnriched> getSpeciesDetailsEnriched(SpeciesDetailsGbifDTO speciesDetailsGbifDTO);
}
