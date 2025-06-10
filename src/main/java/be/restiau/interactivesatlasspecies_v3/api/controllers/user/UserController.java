package be.restiau.interactivesatlasspecies_v3.api.controllers.user;

import be.restiau.interactivesatlasspecies_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivesatlasspecies_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivesatlasspecies_v3.api.models.user.form.UserForm;
import be.restiau.interactivesatlasspecies_v3.bll.services.user.UserService;
import be.restiau.interactivesatlasspecies_v3.dl.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST permettant à l'utilisateur authentifié
 * d'accéder à son profil et de le modifier.
 */
@Tag(name="User")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    /**
     * Récupère le profil de l'utilisateur connecté.
     *
     * @param user L'utilisateur actuellement authentifié.
     * @return Les informations du profil sous forme de DTO.
     */
    @Operation(
            summary = "Obtenir le profil de l'utilisateur",
            description = "Cette opération permet à un utilisateur connecté de récupérer son profil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé")
            }
    )
    @GetMapping
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getById(user.getId()));
    }

    /**
     * Met à jour les informations du profil de l'utilisateur connecté.
     *
     * @param userForm Les nouvelles données du profil.
     * @param user L'utilisateur actuellement authentifié.
     * @return Un DTO contenant un nouveau token utilisateur.
     */
    @Operation(
            summary = "Mettre à jour le profil de l'utilisateur",
            description = "Permet à un utilisateur connecté de mettre à jour ses informations de profil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profil mis à jour avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides"),
                    @ApiResponse(responseCode = "401", description = "Non autorisé")
            }
    )
    @PutMapping
    public ResponseEntity<UserTokenDTO> updateProfile(
            @Valid @RequestBody UserForm userForm,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.updateUser(user.getId(), userForm));
    }
}
