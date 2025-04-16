package be.restiau.interactivespeciesatlas_v3.bll.mappers;

import be.restiau.interactivespeciesatlas_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;

public interface SpeciesMapper {
    Species toSpecies(SpeciesSaveForm speciesForm);
}
