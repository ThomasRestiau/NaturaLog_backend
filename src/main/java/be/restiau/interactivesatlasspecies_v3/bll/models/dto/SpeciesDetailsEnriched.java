package be.restiau.interactivesatlasspecies_v3.bll.models.dto;

import be.restiau.interactivesatlasspecies_v3.api.models.species.dto.SpeciesCoordDTO;

import java.util.List;

public record SpeciesDetailsEnriched(
        String gbifId,
        String scientificName,
        String vernacularName,
        String canonicalName,
        String kingdom,
        String phylum,
        String order,
        String family,
        String genus,
        String species,
        String imageUrl,
        List<String> description,
        List<SpeciesCoordDTO> coords
) {
}
