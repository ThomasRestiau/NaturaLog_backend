package be.restiau.naturalog_backend.bll.services.species;

import be.restiau.naturalog_backend.api.models.species.form.SpeciesSaveForm;
import be.restiau.naturalog_backend.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.naturalog_backend.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.naturalog_backend.dl.entities.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SpeciesService {

    void addSpeciesToCollection(Long userId, SpeciesSaveForm species);
    void removeSpeciesFromCollection(User user, String gbiId);
    List<SpeciesShortGbifDTO> searchByVernacularName(String name);
    Mono<SpeciesDetailsEnriched> getSpeciesDetails(String key) ;
}