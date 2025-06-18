package be.restiau.naturalog_backend.bll.mappers;

import be.restiau.naturalog_backend.api.models.security.dtos.UserShortDTO;
import be.restiau.naturalog_backend.api.models.security.forms.RegisterForm;
import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import be.restiau.naturalog_backend.dl.entities.User;

public interface UserMapper {

    UserDTO userToUserDTO(User user);

    UserShortDTO userToUserShortDTO(User user);

    User registerFormToUser(RegisterForm registerForm);
}
