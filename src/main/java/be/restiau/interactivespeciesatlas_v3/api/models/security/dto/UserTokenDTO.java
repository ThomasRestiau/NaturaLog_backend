package be.restiau.interactivespeciesatlas_v3.api.models.security.dto;

public record UserTokenDTO (
        UserDTO user,
        String token
){
}
