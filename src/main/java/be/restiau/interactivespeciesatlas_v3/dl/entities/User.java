package be.restiau.interactivespeciesatlas_v3.dl.entities;

import be.restiau.interactivespeciesatlas_v3.dl.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Entity
@Table(name = "user_",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user__first_name__last_name__birth_date",
                        columnNames = {"first_name", "last_name","birth_date"}
                )
        }
)
public class User extends BaseEntity<Long> implements UserDetails {

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false, length = 123, name = "first_name")
    private String firstName;

    @Setter
    @Column(nullable = false, length = 80, name = "last_name")
    private String lastName;

    @Setter
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Setter
    @Column(nullable = false, name ="birth_date")
    private LocalDate birth_date;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole role;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_species",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "species_id")
    )
    private Set<Species> speciesSet;

    public User(){
        this.speciesSet = new HashSet<>();
    }

    public User(String username, String password, String firstName, String lastName, String email, LocalDate birthDate) {
        this();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birth_date = birthDate;
    }

    public User(String username, String password, String firstName, String lastName, String email, LocalDate birthDate, UserRole role) {
        this(username, password, firstName, lastName, email, birthDate);
        this.role = role;
    }


    public void addSpecies(Species species){
        if (this.speciesSet == null) {
            this.speciesSet = new HashSet<>();
        }
        this.speciesSet.add(species);
    }

    public void removeSpecies(Species existingSpecies) {
        this.speciesSet.remove(existingSpecies);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
