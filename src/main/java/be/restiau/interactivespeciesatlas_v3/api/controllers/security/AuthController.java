package be.restiau.interactivespeciesatlas_v3.api.controllers.security;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.dto.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.LoginForm;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.bll.services.security.AuthService;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.il.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterForm form
    ) {
        authService.register(form.toUser());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(
            @Valid @RequestBody LoginForm form
    ){
        User user = authService.login(form.username(), form.password());
        UserDTO userDTO = UserDTO.fromUser(user);
        String token = jwtUtil.generateToken(user);
        UserTokenDTO userTokenDTO = new UserTokenDTO(userDTO, token);
        return ResponseEntity.ok(userTokenDTO);
    }
}
