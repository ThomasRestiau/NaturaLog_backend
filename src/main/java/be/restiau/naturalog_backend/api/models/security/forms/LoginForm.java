package be.restiau.naturalog_backend.api.models.security.forms;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank(message="Username is required")
        String username,
        @NotBlank(message="Password is required")
        String password
) {
}
