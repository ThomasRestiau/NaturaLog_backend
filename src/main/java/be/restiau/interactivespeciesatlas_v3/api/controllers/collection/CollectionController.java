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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
    public Mono<ResponseEntity<Page<SpeciesDetailsEnriched>>> getCollection(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String scientificName,
            @RequestParam(required = false) String vernacularName,
            @RequestParam(required = false) String kingdom,
            @RequestParam(required = false) String phylum,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String genus,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Set<SpeciesDTO> speciesSet = user.getSpeciesSet().stream()
                .map(SpeciesDTO::fromSpecies)
                .collect(Collectors.toUnmodifiableSet());

        Flux<SpeciesDetailsEnriched> speciesDetailsFlux = Flux.fromIterable(speciesSet)
                .flatMap(dto -> speciesService.getSpeciesDetails(dto.gbifId()));

        return speciesDetailsFlux
                .filter(s -> {
                    boolean matchesText = (scientificName == null || (s.scientificName() != null &&
                            s.scientificName().toLowerCase().contains(scientificName.toLowerCase())))
                            && (vernacularName == null || (s.vernacularName() != null &&
                            s.vernacularName().toLowerCase().contains(vernacularName.toLowerCase())));

                    boolean matchesTaxonomy = (kingdom == null || kingdom.equalsIgnoreCase(s.kingdom()))
                            && (phylum == null || phylum.equalsIgnoreCase(s.phylum()))
                            && (order == null || order.equalsIgnoreCase(s.order()))
                            && (family == null || family.equalsIgnoreCase(s.family()))
                            && (genus == null || genus.equalsIgnoreCase(s.genus()))
                            && (species == null || species.equalsIgnoreCase(s.species()));

                    boolean matchesCountry = (country == null || s.coords() != null &&
                            s.coords().stream().anyMatch(coord -> country.equalsIgnoreCase(coord.country())));

                    return matchesText && matchesTaxonomy && matchesCountry;
                })
                .collectList()
                .map(list -> {
                    int total = list.size();
                    int start = Math.min(page * size, total);
                    int end = Math.min(start + size, total);
                    List<SpeciesDetailsEnriched> content = list.subList(start, end);
                    Page<SpeciesDetailsEnriched> pageResult = new PageImpl<>(content, PageRequest.of(page, size), total);
                    return ResponseEntity.ok(pageResult);
                })
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
