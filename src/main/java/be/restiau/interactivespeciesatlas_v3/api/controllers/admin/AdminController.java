package be.restiau.interactivespeciesatlas_v3.api.controllers.admin;

import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.bll.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    @Operation(summary = "Renvoie les membres par page (ADMIN)")
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

    @Operation(summary = "Suppression d'un compte (ADMIN)")
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

}
