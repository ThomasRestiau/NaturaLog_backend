package be.restiau.naturalog_backend.il.adapters;

import be.restiau.naturalog_backend.bll.models.dto.SpeciesDetailsGbifDTO;
import be.restiau.naturalog_backend.bll.models.dto.SpeciesShortGbifDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GbifAdapter {

    List<SpeciesShortGbifDTO> searchByVernacularName(String name);
    Mono<SpeciesDetailsGbifDTO> getSpeciesDetailsGbifDTO (String key);
}
