package be.restiau.interactivespeciesatlas_v3.bll.services.user.impls;

import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserShortDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.security.dtos.UserTokenDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.user.dto.UserDTO;
import be.restiau.interactivespeciesatlas_v3.api.models.user.form.UserForm;
import be.restiau.interactivespeciesatlas_v3.bll.mappers.UserMapper;
import be.restiau.interactivespeciesatlas_v3.bll.services.user.UserService;
import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.il.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> all = userRepository.findAll(pageable);
        if (all.isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return all.map(userMapper::userToUserDTO);
    }

    public UserDTO getById(Long id) {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
        return userMapper.userToUserDTO(existingUser);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("User with username " + username + " not found")
        );
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public UserTokenDTO updateUser(Long id, UserForm userForm) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        User updatedUser = userRepository.save(updateFields(existingUser, userForm));
        UserShortDTO updatedShort = userMapper.userToUserShortDTO(updatedUser);
        String newToken = jwtUtil.generateToken(updatedUser);
        return new UserTokenDTO(updatedShort, newToken);
    }

    private User updateFields(User existingUser, UserForm userForm) {
        if (!existingUser.getUsername().equals(userForm.username()) &&
                userRepository.existsByUsername(userForm.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (!existingUser.getEmail().equals(userForm.email()) &&
                userRepository.existsByEmail(userForm.email())) {
            throw new IllegalArgumentException("Email already taken");
        }

        if (userForm.password() != null && !userForm.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userForm.password()));
        }

        existingUser.setUsername(userForm.username());
        existingUser.setFirstName(userForm.firstName());
        existingUser.setLastName(userForm.lastName());
        existingUser.setEmail(userForm.email());
        existingUser.setBirthDate(userForm.birthDate());

        return existingUser;
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

}
