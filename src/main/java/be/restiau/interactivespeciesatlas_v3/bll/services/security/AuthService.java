package be.restiau.interactivespeciesatlas_v3.bll.services.security;

import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    void register(User user);
    User login(String username, String password);

}
