package be.restiau.interactivespeciesatlas_v3.api.controllers.user;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.user.form.UserForm;
import be.restiau.interactivespeciesatlas_v3.bll.services.user.UserService;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getById(user.getId()));
    }

    @PutMapping
    public ResponseEntity<UserTokenDTO> update(
            @Valid @RequestBody UserForm userForm,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.updateUser(user.getId(), userForm));
    }
}
