package be.restiau.naturalog_backend.api.models.security.dtos;

import be.restiau.naturalog_backend.dl.enums.UserRole;

public record UserShortDTO(
        Long id,
        UserRole role,
        String username
){
}
