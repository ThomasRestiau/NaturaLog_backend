package be.restiau.interactivespeciesatlas_v3.api.models;

import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import jakarta.validation.constraints.NotNull;

public record SpeciesForm(
        @NotNull
        String speciesKey,
        @NotNull
        String vernacularName
) {
    public Species toSpecies() {
        return new Species(speciesKey, vernacularName);
    }
}
