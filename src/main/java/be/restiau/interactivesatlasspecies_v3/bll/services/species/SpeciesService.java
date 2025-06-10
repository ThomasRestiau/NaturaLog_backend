package be.restiau.interactivesatlasspecies_v3.bll.services.species;

import be.restiau.interactivesatlasspecies_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivesatlasspecies_v3.dl.entities.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SpeciesService {

    void addSpeciesToCollection(Long userId, SpeciesSaveForm species);
    void removeSpeciesFromCollection(User user, String gbiId);
    List<SpeciesShortGbifDTO> searchByVernacularName(String name);
    Mono<SpeciesDetailsEnriched> getSpeciesDetails(String key) ;
}