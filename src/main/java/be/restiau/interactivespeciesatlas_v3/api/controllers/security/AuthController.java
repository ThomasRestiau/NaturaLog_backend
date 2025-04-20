package be.restiau.interactivespeciesatlas_v3.api.controllers.security;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.LoginForm;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Inscription d'un nouvel utilisateur.
     */
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Inscription d'un nouvel utilisateur",
            description = "Permet à un utilisateur de s'inscrire en fournissant ses informations.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur inscrit avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données d'inscription invalides")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @Valid @RequestBody RegisterForm registerForm) {
        return ResponseEntity.ok(authService.register(registerForm));
    }

    /**
     * Connexion de l'utilisateur et génération d'un token.
     */
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Connexion de l'utilisateur et génération du token",
            description = "Permet à un utilisateur de se connecter et de récupérer un token d'authentification.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Connexion réussie et token généré"),
                    @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(@Valid @RequestBody LoginForm loginForm) {
        return ResponseEntity.ok(authService.login(loginForm.username(), loginForm.password()));
    }
}
