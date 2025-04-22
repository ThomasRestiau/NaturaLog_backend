package be.restiau.interactivespeciesatlas_v3.api.controllers.collection;

import be.restiau.interactivespeciesatlas_v3.api.models.species.dto.SpeciesDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivespeciesatlas_v3.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.interactivespeciesatlas_v3.bll.services.species.SpeciesService;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/me/collection")
@PreAuthorize("isAuthenticated()")
public class CollectionController {

    private final SpeciesService speciesService;

    /**
     * Récupère la collection d'espèces de l'utilisateur connecté.
     */
    @Operation(
            summary = "Récupérer la collection d'espèces de l'utilisateur",
            description = "Cette opération permet à un utilisateur connecté de récupérer sa collection d'espèces sauvegardées.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Collection d'espèces récupérée avec succès"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé")
            }
    )
    @GetMapping
    public Mono<ResponseEntity<Set<SpeciesDetailsEnriched>>> getCollection(@AuthenticationPrincipal User user) {

        Set<SpeciesDTO> speciesSet = user.getSpeciesSet().stream()
                .map(SpeciesDTO::fromSpecies)
                .collect(Collectors.toUnmodifiableSet());

        Flux<SpeciesDetailsEnriched> speciesDetailsFlux = Flux.fromIterable(speciesSet)
                .flatMap(dto -> speciesService.getSpeciesDetails(dto.gbifId()));

        return speciesDetailsFlux.collect(Collectors.toSet())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    /**
     * Ajoute une espèce à la collection de l'utilisateur connecté.
     */
    @Operation(
            summary = "Ajouter une espèce à la collection de l'utilisateur",
            description = "Permet à un utilisateur connecté d'ajouter une espèce à sa collection.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Espèce ajoutée à la collection"),
                    @ApiResponse(responseCode = "400", description = "Données invalides")
            }
    )
    @PostMapping("/{userId}")
    public ResponseEntity<Void> addToCollection(@Valid @RequestBody SpeciesSaveForm form, @PathVariable Long userId) {
        speciesService.addSpeciesToCollection(userId, form);
        return ResponseEntity.status(201).build();
    }

    /**
     * Supprime une espèce spécifique de la collection de l'utilisateur connecté.
     */
    @Operation(
            summary = "Supprimer une espèce de la collection",
            description = "Permet à un utilisateur connecté de supprimer une espèce de sa collection.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Espèce supprimée avec succès"),
                    @ApiResponse(responseCode = "404", description = "Espèce non trouvée")
            }
    )
    @DeleteMapping("/{gbifId}")
    public ResponseEntity<Void> removeFromCollection(@PathVariable String gbifId, @AuthenticationPrincipal User user) {
        speciesService.removeSpeciesFromCollection(user, gbifId);
        return ResponseEntity.noContent().build();
    }
}
