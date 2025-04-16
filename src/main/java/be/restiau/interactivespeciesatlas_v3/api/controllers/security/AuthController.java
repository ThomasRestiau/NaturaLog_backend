package be.restiau.interactivespeciesatlas_v3.api.controllers.security;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.LoginForm;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Inscription d'un nouvel utilisateur.
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @Valid @RequestBody RegisterForm registerForm) {
        return ResponseEntity.ok(authService.register(registerForm));
    }

    /**
     * Connexion de l'utilisateur et génération d'un token.
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(@Valid @RequestBody LoginForm loginForm) {
        return ResponseEntity.ok(authService.login(loginForm.username(), loginForm.password()));
    }
}
