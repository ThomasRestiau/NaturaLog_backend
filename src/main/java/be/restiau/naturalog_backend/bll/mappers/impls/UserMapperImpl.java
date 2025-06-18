package be.restiau.naturalog_backend.bll.mappers.impls;

import be.restiau.naturalog_backend.api.models.security.dtos.UserShortDTO;
import be.restiau.naturalog_backend.api.models.security.forms.RegisterForm;
import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import be.restiau.naturalog_backend.bll.mappers.UserMapper;
import be.restiau.naturalog_backend.dl.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO userToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate()
        );
    }

    @Override
    public UserShortDTO userToUserShortDTO(User user) {
        return new UserShortDTO(
                user.getId(),
                user.getRole(),
                user.getUsername()
        );
    }

    @Override
    public User registerFormToUser(RegisterForm registerForm) {
        return  new User(
                registerForm.username(),
                registerForm.password(),
                registerForm.firstName(),
                registerForm.lastName(),
                registerForm.email(),
                registerForm.birthDate()
        );
    }
}
