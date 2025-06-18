package be.restiau.naturalog_backend.bll.services.user;

import be.restiau.naturalog_backend.api.models.security.dtos.UserTokenDTO;
import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import be.restiau.naturalog_backend.api.models.user.form.UserForm;
import be.restiau.naturalog_backend.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService  {

    Page<UserDTO> findAll(Pageable pageable);
    UserDTO getById(Long id);
    User getByUsername(String username);
    User createUser(User user);
    UserTokenDTO updateUser(Long id, UserForm userForm);
    void deleteById(Long id);


}
