package be.restiau.interactivespeciesatlas_v3.bll.services.species.impls;

import be.restiau.interactivespeciesatlas_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivespeciesatlas_v3.bll.mappers.SpeciesMapper;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.species.SpeciesService;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.SpeciesRepository;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.il.adapters.GbifAdapter;
import be.restiau.interactivespeciesatlas_v3.il.adapters.LlmAdapter;
import be.restiau.interactivespeciesatlas_v3.il.adapters.OpenAIAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeciesServiceImpl implements SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final UserRepository userRepository;

    private final GbifAdapter gbifAdapter;
//    private final LlmAdapter llmAdapter;
    private final OpenAIAdapter openAIAdapter;
    private final SpeciesMapper speciesMapper;

    @Override
    public void addSpeciesToCollection(Long userId, SpeciesSaveForm speciesForm) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " not found")
        );

        Species species = speciesRepository.findByGbifId(speciesForm.gbifId()).stream()
                .findFirst()
                .orElseGet(() -> speciesRepository.save(speciesMapper.toSpecies(speciesForm)));

        if (user.getSpeciesSet().contains(species)) {
            throw new IllegalArgumentException("Species with id " + speciesForm.gbifId() + " already exists in collection");
        }

        user.addSpecies(species);
        userRepository.save(user);
    }

    @Override
    public void removeSpeciesFromCollection(User user, Long speciesId) {
        Species species = speciesRepository.findById(speciesId).orElseThrow(
                () -> new IllegalArgumentException("Species id not found")
        );

        boolean removed = user.getSpeciesSet().removeIf(species::equals);

        if (!removed) {
            throw new IllegalArgumentException("Species not in user's collection");
        }

        userRepository.save(user);
    }

    @Cacheable("vernacularName")
    @Override
    public List<SpeciesShortGbifDTO> searchByVernacularName(String name) {
        return gbifAdapter.searchByVernacularName(name);
    }

    @Cacheable("species")
    @Override
    public Mono<SpeciesDetailsEnriched> getSpeciesDetails(String key) {
        return gbifAdapter.getSpeciesDetailsGbifDTO(key)
                .flatMap(openAIAdapter::getSpeciesDetailsEnriched);
    }
}
