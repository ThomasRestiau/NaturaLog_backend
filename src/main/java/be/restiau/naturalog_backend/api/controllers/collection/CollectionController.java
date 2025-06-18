package be.restiau.naturalog_backend.api.controllers.collection;

import be.restiau.naturalog_backend.api.models.species.dto.SpeciesDTO;
import be.restiau.naturalog_backend.api.models.species.form.SpeciesSaveForm;
import be.restiau.naturalog_backend.bll.models.dto.SpeciesDetailsEnriched;
import be.restiau.naturalog_backend.bll.services.filters.SpeciesFilter;
import be.restiau.naturalog_backend.bll.services.species.SpeciesService;
import be.restiau.naturalog_backend.dl.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Contrôleur REST permettant la gestion de la collection d'espèces de l'utilisateur connecté.
 * Inclut des opérations pour récupérer la collection d'espèces, ajouter ou supprimer des espèces.
 */
@Tag(name="Collection")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/me/collection")
@PreAuthorize("isAuthenticated()")
public class CollectionController {

    private final SpeciesService speciesService;

    /**
     * Récupère la collection d'espèces de l'utilisateur connecté.
     * Cette méthode permet de filtrer les espèces selon divers critères (nom scientifique, nom vernaculaire, etc.).
     *
     * @param user L'utilisateur connecté
     * @param scientificName Filtre par nom scientifique
     * @param vernacularName Filtre par nom vernaculaire
     * @param kingdom Filtre par règne
     * @param phylum Filtre par embranchement
     * @param order Filtre par ordre
     * @param family Filtre par famille
     * @param genus Filtre par genre
     * @param species Filtre par espèce
     * @param country Filtre par pays
     * @param page Numéro de la page (pour la pagination)
     * @param size Nombre d'éléments par page
     * @return La collection d'espèces de l'utilisateur filtrée et paginée
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
        SpeciesFilter filter = new SpeciesFilter()
                .scientificName(scientificName)
                .vernacularName(vernacularName)
                .kingdom(kingdom)
                .phylum(phylum)
                .order(order)
                .family(family)
                .genus(genus)
                .species(species)
                .country(country);

        Predicate<SpeciesDetailsEnriched> predicate = filter.toPredicate();

        Set<SpeciesDTO> speciesSet = user.getSpeciesSet().stream()
                .map(SpeciesDTO::fromSpecies)
                .collect(Collectors.toUnmodifiableSet());

        Flux<SpeciesDetailsEnriched> speciesDetailsFlux = Flux.fromIterable(speciesSet)
                .flatMap(dto -> speciesService.getSpeciesDetails(dto.gbifId()));


        return speciesDetailsFlux
                .filter(predicate)
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
     * Cette méthode permet à un utilisateur d'ajouter une espèce à sa collection personnelle.
     *
     * @param form Formulaire contenant les informations de l'espèce à ajouter
     * @param userId L'identifiant de l'utilisateur connecté
     * @return Le statut HTTP indiquant si l'ajout a réussi
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
     * Cette méthode permet de supprimer une espèce de la collection de l'utilisateur via son GBIF ID.
     *
     * @param gbifId L'ID de l'espèce à supprimer
     * @param user L'utilisateur connecté
     * @return Le statut HTTP indiquant si la suppression a réussi
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
