package be.restiau.interactivespeciesatlas_v3.bll.services.security;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    UserDTO register(RegisterForm registerForm);
    UserTokenDTO login(String username, String password);

}
