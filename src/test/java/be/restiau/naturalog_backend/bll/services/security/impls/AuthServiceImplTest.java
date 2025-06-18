package be.restiau.naturalog_backend.bll.services.security.impls;

import be.restiau.naturalog_backend.api.models.security.dtos.UserShortDTO;
import be.restiau.naturalog_backend.api.models.security.dtos.UserTokenDTO;
import be.restiau.naturalog_backend.api.models.security.forms.RegisterForm;
import be.restiau.naturalog_backend.api.models.user.dto.UserDTO;
import be.restiau.naturalog_backend.bll.exceptions.user.EmailAlreadyExistsException;
import be.restiau.naturalog_backend.bll.exceptions.user.UsernameAlreadyExistsException;
import be.restiau.naturalog_backend.bll.mappers.UserMapper;
import be.restiau.naturalog_backend.dal.repositories.UserRepository;
import be.restiau.naturalog_backend.dl.entities.User;
import be.restiau.naturalog_backend.dl.enums.UserRole;
import be.restiau.naturalog_backend.il.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterForm registerForm;
    private User user;
    private User savedUser;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        registerForm = new RegisterForm(
                "jdoe", "password123", "John", "Doe", "jdoe@email.com", LocalDate.of(1990, 1, 1)
        );

        user = new User(
                "jdoe", "password123", "John", "Doe", "jdoe@email.com", LocalDate.of(1990, 1, 1)
        );

        savedUser = new User(
                "jdoe", "encodedPassword", "John", "Doe", "jdoe@email.com", LocalDate.of(1990, 1, 1)
        );

//        savedUser.setId(1L); // Décommenter le @Setter dans dl/entities/BaseEntity
        savedUser.setRole(UserRole.USER);

        userDTO = new UserDTO(1L, "JohnDoe", "John","Doe", "jdoe@email.com", LocalDate.of(1990, 1, 1));
    }

    // ---------- TESTS REGISTER ----------

    @Test
    void register_shouldSaveUserAndReturnUserDTO() {
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@email.com")).thenReturn(false);
        when(userMapper.registerFormToUser(registerForm)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.userToUserDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = authService.register(registerForm);

        assertNotNull(result);
        assertEquals("jdoe", result.username());
        verify(userRepository).save(argThat(u ->
                u.getPassword().equals("encodedPassword") && u.getRole() == UserRole.USER
        ));
    }

    @Test
    void register_shouldThrowWhenUsernameExists() {
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        UsernameAlreadyExistsException ex = assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.register(registerForm);
        });

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowWhenEmailExists() {
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@email.com")).thenReturn(true);

        EmailAlreadyExistsException ex = assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(registerForm);
        });

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(userRepository, never()).save(any());
    }

    // ---------- TESTS LOGIN ----------

    @Test
    void login_shouldReturnUserTokenDTO_whenCredentialsAreCorrect() {
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches("password123", savedUser.getPassword())).thenReturn(true);
        when(userMapper.userToUserShortDTO(savedUser)).thenReturn(
                new UserShortDTO(1L, UserRole.USER, "John Doe")
        );
        when(jwtUtil.generateToken(savedUser)).thenReturn("token123");

        UserTokenDTO result = authService.login("jdoe", "password123");

        assertNotNull(result);
        assertEquals("token123", result.token());
    }

    @Test
    void login_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login("jdoe", "password123");
        });
    }

    @Test
    void login_shouldThrow_whenPasswordDoesNotMatch() {
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches("password123", savedUser.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authService.login("jdoe", "password123");
        });
    }

    // ---------- TESTS LOAD USER ----------

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(savedUser));

        User result = (User) authService.loadUserByUsername("jdoe");

        assertEquals("jdoe", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrow_whenUserDoesNotExist() {
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("jdoe");
        });
    }

    @Test
    void register_shouldThrowConflict_whenUsernameAlreadyExists() {
        RegisterForm form = new RegisterForm("john", "pass", "John", "Doe", "john@mail.com", LocalDate.of(1990, 1, 1));
        when(userRepository.existsByUsername("john")).thenReturn(true);

        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(form));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Username already exists", exception.getError());
    }

    @Test
    void register_shouldThrowConflict_whenEmailAlreadyExists() {
        RegisterForm form = new RegisterForm("john", "pass", "John", "Doe", "john@mail.com", LocalDate.of(1990, 1, 1));
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@mail.com")).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> authService.register(form));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Email already exists", exception.getError());
    }

    @Test
    void login_shouldThrow_whenUsernameNotFound() {
        when(userRepository.findByUsername("inexistant")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authService.login("inexistant", "pass"));

        assertEquals("inexistant does not exist", exception.getMessage());
    }

    @Test
    void login_shouldThrow_whenBadPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.login("john", "wrongPassword"));

        assertEquals("Bad credentials", exception.getMessage());
    }

}
