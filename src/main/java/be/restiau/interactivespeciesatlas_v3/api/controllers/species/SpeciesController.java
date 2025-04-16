package be.restiau.interactivespeciesatlas_v3.api.controllers.species;

import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.species.SpeciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/search")
    public ResponseEntity<List<SpeciesShortGbifDTO>> searchSpeciesByVernacularName(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(speciesService.searchByVernacularName(name));
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping()
    public ResponseEntity<SpeciesDetailsEnriched> getSpeciesDetails(
            @RequestParam String key
    ) {
        SpeciesDetailsEnriched speciesDetailsEnriched = speciesService.getSpeciesDetails(key).block();
        return ResponseEntity.ok(speciesDetailsEnriched);
    }
}
