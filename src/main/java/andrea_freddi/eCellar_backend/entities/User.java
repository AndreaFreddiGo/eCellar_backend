package andrea_freddi.eCellar_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// creates the User class and manages Getter and Setter and empty constructor with lombok
// I manage the constructor myself as well as toString
// implements UserDetails which will allow me to manage different roles.
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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String biography;
    private String phone;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "preferred_language")
    private String preferredLanguage;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(name = "public_profile", nullable = false)
    private boolean publicProfile;
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    // relations between User and other entities: CellarWine, Cellar and Address
    // a User can have multiple CellarWines, multiple Cellars and multiple Addresses

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CellarWine> cellarWines;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cellar> cellars;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;


    public User(String name, String surname, String email, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Role.USER; // everyone is created as a USER by default
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
                ", username='" + username + '\'' +
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
        return this.username; // this method is used by Spring Security to get the username
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

    // These methods are used to check if the user is an admin or the owner of a Cellar, Address or CellarWine
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isOwnerOf(Cellar cellar) {
        return cellar.getUser().getId().equals(this.getId());
    }

    public boolean isOwnerOf(Address address) {
        return address.getUser().getId().equals(this.getId());
    }

    public boolean isOwnerOf(CellarWine cellarWine) {
        return cellarWine.getUser().getId().equals(this.getId());
    }

}
