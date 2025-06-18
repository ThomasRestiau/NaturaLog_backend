package be.restiau.naturalog_backend.dal.repositories;

import be.restiau.naturalog_backend.dl.entities.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

    Optional<Species> findByGbifId(String gbifId);
    boolean existsByGbifId(String gbifId);

}
