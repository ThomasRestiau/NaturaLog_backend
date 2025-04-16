package be.restiau.interactivespeciesatlas_v3.bll.mappers.impls;

import be.restiau.interactivespeciesatlas_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivespeciesatlas_v3.bll.mappers.SpeciesMapper;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMapperImpl implements SpeciesMapper {

    @Override
    public Species toSpecies(SpeciesSaveForm speciesForm) {
        return new Species(
                speciesForm.gbifId(),
                speciesForm.scientificName(),
                speciesForm.commonName(),
                speciesForm.userNotes()
        );
    }

}
