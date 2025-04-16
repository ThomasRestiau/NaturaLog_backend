package be.restiau.interactivespeciesatlas_v3.il.adapters;

import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsGbifDTO;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesShortGbifDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GbifAdapter {

    List<SpeciesShortGbifDTO> searchByVernacularName(String name);
    Mono<SpeciesDetailsGbifDTO> getSpeciesDetailsGbifDTO (String key);
}
