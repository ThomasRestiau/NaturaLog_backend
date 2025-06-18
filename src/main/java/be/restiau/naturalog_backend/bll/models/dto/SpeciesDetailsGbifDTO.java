package be.restiau.naturalog_backend.bll.models.dto;

import be.restiau.naturalog_backend.api.models.species.dto.SpeciesCoordDTO;

import java.util.List;

public record SpeciesDetailsGbifDTO(
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
        List<SpeciesCoordDTO> coords
) {
}
