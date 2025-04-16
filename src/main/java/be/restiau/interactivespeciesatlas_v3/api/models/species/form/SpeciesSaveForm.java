package be.restiau.interactivespeciesatlas_v3.api.models.species.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpeciesSaveForm(
        @NotBlank
        String scientificName,
        @NotBlank
        String commonName,
        @NotBlank
        String gbifId,
        @Size(max = 3000)
        String userNotes
) {
}
