package be.restiau.naturalog_backend.api.models.security.dtos;

public record UserTokenDTO (
        UserShortDTO user,
        String token
){
}
