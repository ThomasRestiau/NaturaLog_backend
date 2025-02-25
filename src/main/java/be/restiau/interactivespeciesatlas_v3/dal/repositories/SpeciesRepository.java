package be.restiau.interactivespeciesatlas_v3.dal.repositories;

import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

    Optional<Species> findBySpeciesKey(String speciesKey);
    boolean existsBySpeciesKey(String speciesKey);

}
