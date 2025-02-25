package be.restiau.interactivespeciesatlas_v3.api.controllers;

import be.restiau.interactivespeciesatlas_v3.api.models.SpeciesForm;
import be.restiau.interactivespeciesatlas_v3.bll.services.SpeciesService;
import be.restiau.interactivespeciesatlas_v3.bll.services.UserService;
import be.restiau.interactivespeciesatlas_v3.dl.entities.Species;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SpeciesService speciesService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/collection")
    public ResponseEntity<Set<Species>> getCollection(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Set<Species> speciesSet = userService.getById(user.getId()).getSpeciesSet();

        return ResponseEntity.ok(speciesSet);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/saveSpecies")
    public ResponseEntity<Void> saveSpecies(
            @Valid @RequestBody SpeciesForm form,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) authentication.getPrincipal();
        speciesService.addSpecies(user,form.toSpecies());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteSpecies")
    public ResponseEntity<Void> deleteSpecies(
            @Valid @RequestBody SpeciesForm form,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) authentication.getPrincipal();
        speciesService.removeSpecies(user,form.toSpecies());

        return ResponseEntity.ok().build();
    }
}
