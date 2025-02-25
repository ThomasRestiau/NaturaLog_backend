package be.restiau.interactivespeciesatlas_v3.bll.services.impls;

import be.restiau.interactivespeciesatlas_v3.bll.services.SpeciesService;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.SpeciesRepository;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpeciesServiceImpl implements SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final UserRepository userRepository;


    @Override
    public void addSpecies(User user, Species species) {

        Species existingSpecies = speciesRepository.findBySpeciesKey(species.getSpeciesKey()).orElse(null);

        if (existingSpecies == null) {
            existingSpecies = speciesRepository.save(species);
        }

        user.addSpecies(existingSpecies);
        userRepository.save(user);
    }

    @Override
    public void removeSpecies(User user, Species species) {
        if (species == null || species.getSpeciesKey() == null) {
            throw new IllegalArgumentException("Species key cannot be null");
        }

        Species speciesEntity = speciesRepository.findBySpeciesKey(species.getSpeciesKey()).orElseThrow(
                () -> new IllegalArgumentException("Species key not found"));

        user.getSpeciesSet().remove(speciesEntity);

        userRepository.save(user);
    }
}
