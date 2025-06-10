package be.restiau.interactivesatlasspecies_v3.api.models.security.dtos;

public record UserTokenDTO (
        UserShortDTO user,
        String token
){
}
