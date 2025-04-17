package andrea_freddi.CAPSTONE_PROJECT.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// creates the User class and manages Getter and Setter and empty constructor with lombok
// I manage the constructor myself as well as toString
// implements UserDetails which will allow me to manage different roles
// the implementation also required the override of some methods

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(name = "public_profile", nullable = false)
    private boolean publicProfile;
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    // relations between User and other entities: CellarWine and Cellar
    // a User can have multiple CellarWines and multiple Cellars

    @OneToMany(mappedBy = "user")
    private List<CellarWine> cellarWines;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cellar> cellars;


    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = Role.USER; // everyone is created as USER by default
        this.publicProfile = true; // default value for public profile
        this.registrationDate = LocalDateTime.now(); // set registration date to today
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", publicProfile=" + publicProfile +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail(); // this method is used by Spring Security to get the email instead of the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // I don't want to expire the account
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // I don't want to lock the account
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // I don't want to expire the credentials
    }

    @Override
    public boolean isEnabled() {
        return true; // I don't want to disable the account
    }
}
