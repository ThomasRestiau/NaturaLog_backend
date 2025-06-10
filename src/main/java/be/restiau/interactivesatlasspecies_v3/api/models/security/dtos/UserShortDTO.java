package be.restiau.interactivesatlasspecies_v3.api.models.security.dtos;

import be.restiau.interactivesatlasspecies_v3.dl.enums.UserRole;

public record UserShortDTO(
        Long id,
        UserRole role,
        String username
){
}
