package be.restiau.naturalog_backend.api.models.species.dto;

public record SpeciesCoordDTO(
        String latitude,
        String longitude,
        String country
) {
}
