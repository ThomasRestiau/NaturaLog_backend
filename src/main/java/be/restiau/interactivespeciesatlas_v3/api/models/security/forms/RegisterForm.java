package be.restiau.interactivespeciesatlas_v3.api.models.security.forms;

import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterForm(
        @NotBlank @Size(max=50)
        String username,
        @NotBlank
        String password,
        @NotBlank @Size(max=123)
        String firstName,
        @NotBlank @Size(max=80)
        String lastName,
        @NotBlank @Email @Size(max=150)
        String email,
        LocalDate birthDate
) {
    public User toUser(){
        return new User(
                username,
                password,
                firstName,
                lastName,
                email,
                birthDate
        );
    }
}
