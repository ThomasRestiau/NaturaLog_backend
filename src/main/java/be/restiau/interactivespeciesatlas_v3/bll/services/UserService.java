package be.restiau.interactivespeciesatlas_v3.bll.services;

import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService  {

    Page<User> findAll (Pageable pageable);
    User getById(Long id);
    User getByUsername(String username);
    User createUser(User user);
    void updateUser(Long id, User user);
    void deleteById(Long id);
}
