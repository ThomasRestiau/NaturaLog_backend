package be.restiau.interactivespeciesatlas_v3.bll.models.dto;

public record SpeciesShortGbifDTO(
        String gbifId,
        String scientificName,
        String vernacularName,
        String canonicalName

) {
}
