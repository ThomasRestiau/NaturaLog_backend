package be.restiau.interactivesatlasspecies_v3.api.controllers.species;

import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivesatlasspecies_v3.bll.models.dto.SpeciesShortGbifDTO;
import be.restiau.interactivesatlasspecies_v3.bll.services.species.SpeciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST permettant la recherche et la consultation
 * des espèces via le nom vernaculaire ou leur identifiant GBIF.
 */
@Tag(name="Species")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/species")
public class SpeciesController {

    private final SpeciesService speciesService;

    /**
     * Recherche des espèces par leur nom vernaculaire.
     *
     * @param vernacularName Le nom commun de l'espèce à rechercher.
     * @return Une liste d'espèces correspondant au nom donné.
     */
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
            @RequestParam String vernacularName
    ) {
        return ResponseEntity.ok(speciesService.searchByVernacularName(vernacularName));
    }

    /**
     * Récupère les détails d'une espèce à partir de sa clé GBIF.
     *
     * @param speciesKey La clé GBIF de l'espèce.
     * @return Un objet contenant les informations enrichies de l'espèce.
     */
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
