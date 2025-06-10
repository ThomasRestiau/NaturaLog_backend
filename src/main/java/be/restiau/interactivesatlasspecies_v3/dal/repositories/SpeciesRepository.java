package be.restiau.interactivesatlasspecies_v3.dal.repositories;

import be.restiau.interactivesatlasspecies_v3.dl.entities.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

    Optional<Species> findByGbifId(String gbifId);
    boolean existsByGbifId(String gbifId);

}
