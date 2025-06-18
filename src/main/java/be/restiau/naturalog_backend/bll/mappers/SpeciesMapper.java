package be.restiau.naturalog_backend.bll.mappers;

import be.restiau.naturalog_backend.api.models.species.form.SpeciesSaveForm;
import be.restiau.naturalog_backend.dl.entities.Species;

public interface SpeciesMapper {
    Species toSpecies(SpeciesSaveForm speciesForm);
}
