package be.restiau.naturalog_backend.bll.services.security;

import be.restiau.naturalog_backend.api.models.security.dtos.UserTokenDTO;
import be.restiau.naturalog_backend.api.models.security.forms.RegisterForm;
import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    UserDTO register(RegisterForm registerForm);
    UserTokenDTO login(String username, String password);

}
