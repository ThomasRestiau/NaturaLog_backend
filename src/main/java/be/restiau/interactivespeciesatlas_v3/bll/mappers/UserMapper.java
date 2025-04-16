package be.restiau.interactivespeciesatlas_v3.bll.mappers;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserShortDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.forms.RegisterForm;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;

public interface UserMapper {

    UserDTO userToUserDTO(User user);

    UserShortDTO userToUserShortDTO(User user);

    User registerFormToUser(RegisterForm registerForm);
}
