package be.restiau.interactivespeciesatlas_v3.api.models.security.dtos;

public record UserTokenDTO (
        UserShortDTO user,
        String token
){
}
