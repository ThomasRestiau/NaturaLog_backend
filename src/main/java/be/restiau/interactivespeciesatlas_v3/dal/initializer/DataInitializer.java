package be.restiau.interactivespeciesatlas_v3.dal.initializer;

import be.restiau.interactivespeciesatlas_v3.dal.repositories.UserRepository;
import be.restiau.interactivespeciesatlas_v3.dl.entities.User;
import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            String password = passwordEncoder.encode("Test1234=");

            List<User> users = List.of(
                    new User(
                            "admin",
                            password,
                            "admin",
                            "admin",
                            "admin@admin",
                            LocalDate.now(),
                            UserRole.ADMIN),

                    new User(
                            "user",
                            password,
                            "user",
                            "user",
                            "user@user",
                            LocalDate.now(),
                            UserRole.USER),

                    new User(
                            "moderator",
                            password,
                            "moderator",
                            "moderator",
                            "moderator@moderator",
                            LocalDate.now(),
                            UserRole.MODERATOR)
            );
            userRepository.saveAll(users);
        }
    }
}
