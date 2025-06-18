package be.restiau.naturalog_backend.api.controllers.admin;

import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import be.restiau.naturalog_backend.bll.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur dédié aux fonctionnalités administratives.
 * Ce contrôleur permet à un administrateur de gérer les utilisateurs du système.
 * Il est sécurisé pour être accessible uniquement par des utilisateurs ayant le rôle 'ADMIN'.
 */
@Tag(name="Admin")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    /**
     * Récupère la liste des utilisateurs par page.
     * Cette méthode permet à un administrateur de voir les utilisateurs enregistrés dans le système, avec pagination.
     *
     * @param page Le numéro de la page à récupérer (par défaut 0).
     * @param size Le nombre d'éléments par page (par défaut 10).
     * @return Une réponse contenant une page d'utilisateurs.
     */
    @Operation(
            summary = "Récupérer les utilisateurs par page",
            description = "Cette opération permet à un administrateur de récupérer la liste des utilisateurs du système, paginée.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé"),
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Supprime un utilisateur du système par son identifiant.
     * Cette méthode permet à un administrateur de supprimer un compte utilisateur.
     *
     * @param userId L'ID de l'utilisateur à supprimer.
     * @return Une réponse HTTP sans contenu (code 204) après suppression réussie.
     */
    @Operation(
            summary = "Supprimer un compte utilisateur",
            description = "Cette opération permet à un administrateur de supprimer un compte utilisateur par son identifiant.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

}
