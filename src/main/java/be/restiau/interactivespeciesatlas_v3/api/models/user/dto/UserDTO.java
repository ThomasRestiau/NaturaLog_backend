package be.restiau.interactivespeciesatlas_v3.api.models.user.dto;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate
) {
}
