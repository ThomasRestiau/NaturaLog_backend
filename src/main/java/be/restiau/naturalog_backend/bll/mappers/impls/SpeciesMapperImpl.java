package be.restiau.naturalog_backend.bll.mappers.impls;

import be.restiau.naturalog_backend.api.models.species.form.SpeciesSaveForm;
import be.restiau.naturalog_backend.bll.mappers.SpeciesMapper;
import be.restiau.naturalog_backend.dl.entities.Species;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMapperImpl implements SpeciesMapper {

    @Override
    public Species toSpecies(SpeciesSaveForm speciesForm) {
        return new Species(
                speciesForm.gbifId(),
                speciesForm.scientificName(),
                speciesForm.vernacularName(),
                speciesForm.canonicalName()
        );
    }

}
