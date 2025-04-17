package be.restiau.interactivespeciesatlas_v3.api.controllers.species;

import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.species.SpeciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    /**
     * Recherche des espèces par leur nom vernaculaire.
     */
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Recherche des espèces par leur nom vernaculaire",
            description = "Permet de rechercher des espèces en fonction de leur nom vernaculaire.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste d'espèces trouvées"),
                    @ApiResponse(responseCode = "404", description = "Aucune espèce trouvée")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<SpeciesShortGbifDTO>> searchSpeciesByVernacularName(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(speciesService.searchByVernacularName(name));
    }

    /**
     * Récupération des détails d'une espèce.
     */
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Récupérer les détails d'une espèce",
            description = "Permet d'obtenir des informations détaillées sur une espèce en fonction de sa clé.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Détails de l'espèce"),
                    @ApiResponse(responseCode = "404", description = "Espèce non trouvée")
            }
    )
    @GetMapping("/{speciesKey}")
    public ResponseEntity<SpeciesDetailsEnriched> getSpeciesDetails(
            @PathVariable String speciesKey
    ) {
        SpeciesDetailsEnriched speciesDetailsEnriched = speciesService.getSpeciesDetails(speciesKey).block();
        return ResponseEntity.ok(speciesDetailsEnriched);
    }
}
