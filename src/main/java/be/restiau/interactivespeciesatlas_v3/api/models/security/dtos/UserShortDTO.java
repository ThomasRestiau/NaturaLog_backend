package be.restiau.interactivespeciesatlas_v3.api.models.security.dtos;

import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;

public record UserShortDTO(
        Long id,
        UserRole role,
        String fullName
){
}
