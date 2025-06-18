package be.restiau.naturalog_backend.bll.models.dto;

public record SpeciesShortGbifDTO(
        String gbifId,
        String scientificName,
        String vernacularName,
        String canonicalName

) {
}
