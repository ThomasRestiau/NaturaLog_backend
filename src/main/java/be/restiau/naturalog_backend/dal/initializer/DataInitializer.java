package be.restiau.naturalog_backend.dal.initializer;

import be.restiau.naturalog_backend.dal.repositories.UserRepository;
import be.restiau.naturalog_backend.dl.entities.User;
import be.restiau.naturalog_backend.dl.enums.UserRole;
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
    public void run(String... args) {
        if (userRepository.count() == 0) {
            String password = passwordEncoder.encode("Test1234=");

            List<User> users = List.of(
                    new User("adminzilla", password, "Gorgoth", "AdminZilla", "gorgoth@darknet.gov", LocalDate.now().minusDays(3), UserRole.ADMIN),

                    new User("modpixie", password, "Celestina", "Pixieblossom", "celestina@rainmail.com", LocalDate.now().minusDays(4), UserRole.MODERATOR),

                    new User("neo", password, "Thomas", "Anderson", "neo@matrix.io", LocalDate.now().minusDays(5), UserRole.USER),
                    new User("rickdanger", password, "Rick", "Danger", "rick.danger@multiverse.org", LocalDate.now().minusDays(6), UserRole.USER),
                    new User("sushininja", password, "Akira", "Wasabi", "akira@ninjutsu.jp", LocalDate.now().minusDays(7), UserRole.USER),
                    new User("moonwalker", password, "Luna", "Skydancer", "luna@moonmail.com", LocalDate.now().minusDays(8), UserRole.USER),
                    new User("grimjim", password, "Jim", "Grimble", "jim.grim@cryptmail.net", LocalDate.now().minusDays(9), UserRole.USER),
                    new User("quantumbob", password, "Bob", "Q. Particle", "bob@quantumverse.io", LocalDate.now().minusDays(10), UserRole.USER),
                    new User("zeldablade", password, "Linka", "Hyrulia", "linka@hyrule.org", LocalDate.now().minusDays(11), UserRole.USER),
                    new User("floraflame", password, "Dahlia", "Blazewood", "dahlia@petalmail.com", LocalDate.now().minusDays(12), UserRole.USER),
                    new User("crunchbyte", password, "Chad", "AlgoCrunch", "chad.crunch@codehole.dev", LocalDate.now().minusDays(13), UserRole.USER),
                    new User("elgato", password, "Fernando", "El Gato", "fernando@gatos.net", LocalDate.now().minusDays(14), UserRole.USER),
                    new User("icequeen", password, "Elsa", "Stormborn", "elsa@frozenmail.no", LocalDate.now().minusDays(15), UserRole.USER),
                    new User("dirtwizard", password, "Grogg", "Soilmancer", "grogg@mudmagic.org", LocalDate.now().minusDays(16), UserRole.USER),
                    new User("mechabob", password, "Bob", "The Cyborg", "mechabob@future.ai", LocalDate.now().minusDays(17), UserRole.USER),
                    new User("synthwave", password, "Cass", "Neonshade", "cass@retrofuture.vhs", LocalDate.now().minusDays(18), UserRole.USER),
                    new User("pineapplejoe", password, "Joe", "Pineapple", "joe@tropics.fun", LocalDate.now().minusDays(19), UserRole.USER),
                    new User("glitchwitch", password, "Hexa", "Wyrm", "hexawyrm@void.craft", LocalDate.now().minusDays(20), UserRole.USER)
            );

            userRepository.saveAll(users);
        }
    }
}
