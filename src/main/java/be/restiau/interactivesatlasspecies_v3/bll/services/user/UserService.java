package be.restiau.interactivesatlasspecies_v3.bll.services.user;

import be.restiau.interactivesatlasspecies_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivesatlasspecies_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivesatlasspecies_v3.api.models.user.form.UserForm;
import be.restiau.interactivesatlasspecies_v3.dl.entities.User;
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
