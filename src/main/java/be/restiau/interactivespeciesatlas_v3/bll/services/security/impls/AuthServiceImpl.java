package be.restiau.interactivespeciesatlas_v3.bll.services.security.impls;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserShortDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.bll.exceptions.user.BirthDateAfterNowException;
import be.restiau.interactivespeciesatlas_v3.bll.exceptions.user.EmailAlreadyExistsException;
import be.restiau.interactivespeciesatlas_v3.bll.exceptions.user.UsernameAlreadyExistsException;
import be.restiau.interactivespeciesatlas_v3.bll.mappers.UserMapper;
import be.restiau.interactivespeciesatlas_v3.bll.services.security.AuthService;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;
import be.restiau.interactivespeciesatlas_v3.il.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public UserDTO register(RegisterForm registerForm) {
        if(userRepository.existsByUsername(registerForm.username())) {
            throw new UsernameAlreadyExistsException(HttpStatus.CONFLICT, "Username already exists");
        }
        if(userRepository.existsByEmail(registerForm.email())) {
            throw new EmailAlreadyExistsException(HttpStatus.CONFLICT, "Email already exists");
        }
        if(registerForm.birthDate().isAfter(LocalDate.now()) || registerForm.birthDate().isEqual(LocalDate.now())) {
            throw new BirthDateAfterNowException(HttpStatus.CONFLICT, "Birthdate must be before today");
        }
        User newUser = userMapper.registerFormToUser(registerForm);
        newUser.setPassword(passwordEncoder.encode(registerForm.password()));
        newUser.setRole(UserRole.USER);
        return userMapper.userToUserDTO(userRepository.save(newUser));
    }

    @Override
    public UserTokenDTO login(String username, String password) {
        User existingUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username+" does not exist")
        );
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        UserShortDTO userShortDTO = userMapper.userToUserShortDTO(existingUser);
        String token = jwtUtil.generateToken(existingUser);
        return new UserTokenDTO(userShortDTO, token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username+" does not exist")
        );
    }
}
