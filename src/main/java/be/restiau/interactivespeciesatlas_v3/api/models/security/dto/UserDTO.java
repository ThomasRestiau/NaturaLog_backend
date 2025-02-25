package be.restiau.interactivespeciesatlas_v3.api.models.security.dto;

import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;

public record UserDTO (
        Long id,
        UserRole role,
        String fullName
){
    public static UserDTO fromUser(User user) {
        String fullName = user.getFirstName()+" "+user.getLastName();
        return new UserDTO(
                user.getId(),
                user.getRole(),
                fullName
        );
    }
}
