package be.restiau.interactivesatlasspecies_v3.api.models.security.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterForm(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters long")
        String password,

        @NotBlank(message = "First name is required")
        @Size(min=3,max=123, message="First name must be at least 3 characters long")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min=3,max=80, message="Last name must be at least 3 characters long")
        String lastName,

        @NotBlank(message="Email is required")
        @Email(message="Invalid email address")
        String email,

        @NotNull
        LocalDate birthDate
) {
}
