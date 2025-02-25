package be.restiau.interactivespeciesatlas_v3.bll.services.security.impls;

import be.restiau.interactivespeciesatlas_v3.bll.services.security.AuthService;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername()+" already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User existingUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username+" does not exist")
        );
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        return existingUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username+" does not exist")
        );
    }
}
