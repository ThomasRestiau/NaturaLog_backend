package be.restiau.interactivespeciesatlas_v3.api.controllers.user;

import be.restiau.interactivespeciesatlas_v3.api.models.species.form.SpeciesSaveForm;
import be.restiau.interactivespeciesatlas_v3.bll.services.species.SpeciesService;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/collection")
@PreAuthorize("isAuthenticated()")
public class UserCollectionController {

    private final SpeciesService speciesService;

    /**
     * Récupère la collection d'espèces de l'utilisateur connecté
     */
    @GetMapping
    public ResponseEntity<Set<Species>> getCollection(@AuthenticationPrincipal User user) {
        Set<Species> speciesSet = user.getSpeciesSet();
        return ResponseEntity.ok(speciesSet);
    }

    /**
     * Ajoute une espèce à la collection de l'utilisateur connecté
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Void> addToCollection(@Valid @RequestBody SpeciesSaveForm form, @PathVariable Long userId) {
        speciesService.addSpeciesToCollection(userId, form);
        return ResponseEntity.status(201).build();
    }

    /**
     * Supprime une espèce spécifique de la collection de l'utilisateur connecté
     */
    @DeleteMapping("/{speciesId}")
    public ResponseEntity<Void> removeFromCollection(@PathVariable Long speciesId, @AuthenticationPrincipal User user) {
        speciesService.removeSpeciesFromCollection(user, speciesId);
        return ResponseEntity.noContent().build();
    }
}
