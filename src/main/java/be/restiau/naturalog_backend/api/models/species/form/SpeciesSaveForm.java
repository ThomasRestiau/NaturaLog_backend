package be.restiau.naturalog_backend.api.models.species.form;

import jakarta.validation.constraints.NotBlank;

public record SpeciesSaveForm(
        @NotBlank
        String gbifId,
        @NotBlank
        String scientificName,
        @NotBlank
        String vernacularName,
        String canonicalName
) {
}
