package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.PasswordUpdatePayload;
import andrea_freddi.CAPSTONE_PROJECT.payloads.UserPayload;
import andrea_freddi.CAPSTONE_PROJECT.payloads.UserUpdatePayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

// This class is a service that interacts with the UsersRepository to perform operations related to users
// It is annotated with @Service to indicate that it is a service component in the Spring framework

@Service
public class UsersService {

    // The UsersRepository is injected into this service using Spring's dependency injection
    @Autowired
    private UsersRepository usersRepository;

    // The PasswordEncoder is injected into this service to handle password encryption
    @Autowired
    private PasswordEncoder bcrypt;

    // This method finds a user by their id
    public User findById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(
                () -> new BadRequestException("User with id " + userId + " not found!")
        );
    }

    // This method finds a user by their email
    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("User with email " + email + " not found!")
        );
    }

    // This method finds a user by their username
    public User findByUsername(String username) {
        return this.usersRepository.findByUsername(username.toLowerCase()).orElseThrow(
                () -> new BadRequestException("User with username " + username + " not found!")
        );
    }

    // This method finds all users with pagination
    public Page<User> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepository.findAll(pageable);
    }

    // This method saves a new user
    public User save(UserPayload body) {
        String normalizedEmail = body.email().toLowerCase();
        String normalizedUsername = body.username().toLowerCase();

        // Check if email already exists (case-insensitive)
        this.usersRepository.findByEmail(normalizedEmail).ifPresent(existingUser -> {
            throw new BadRequestException("Email " + normalizedEmail + " already in use!");
        });

        // Check if username already exists (case-insensitive)
        this.usersRepository.findByUsername(normalizedUsername).ifPresent(existingUser -> {
            throw new BadRequestException("Username '" + normalizedUsername + "' is already taken!");
        });

        // Create and save the new user
        User newUser = new User(
                body.name(),
                body.surname(),
                normalizedEmail,
                normalizedUsername,
                bcrypt.encode(body.password())
        );

        return this.usersRepository.save(newUser);
    }

    // This method updates an existing user
    public User findByIdAndUpdate(UUID id, UserUpdatePayload body) {
        User found = this.findById(id);
        String normalizedEmail = body.email().toLowerCase();
        String normalizedUsername = body.username().toLowerCase();

        // Check if email is being changed and already exists
        if (!found.getEmail().equalsIgnoreCase(normalizedEmail)) {
            this.usersRepository.findByEmail(normalizedEmail).ifPresent(existingUser -> {
                throw new BadRequestException("Email " + normalizedEmail + " already in use!");
            });
        }

        // Check if the username is being changed and already exists
        if (!found.getUsername().equalsIgnoreCase(normalizedUsername)) {
            this.usersRepository.findByUsername(normalizedUsername).ifPresent(existingUser -> {
                throw new BadRequestException("Username '" + normalizedUsername + "' is already taken!");
            });
        }

        // Update all fields
        found.setName(body.name());
        found.setSurname(body.surname());
        found.setEmail(normalizedEmail);
        found.setUsername(normalizedUsername);
        found.setProfilePicture(body.profilePicture());
        found.setPhone(body.phone());
        found.setBiography(body.biography());
        found.setBirthDate(body.birthDate());
        found.setPreferredLanguage(body.preferredLanguage());
        found.setPublicProfile(body.publicProfile());

        return this.usersRepository.save(found);
    }


    // This method updates a user's password
    public User findByIdAndUpdatePassword(UUID id, PasswordUpdatePayload body) {
        User found = this.findById(id);
        // Check if the old password matches the current password
        if (!bcrypt.matches(body.currentPassword(), found.getPassword())) {
            throw new BadRequestException("Current password is incorrect!");
        }
        // Check if the new password is different from the current password
        if (bcrypt.matches(body.newPassword(), found.getPassword())) {
            throw new BadRequestException("New password must be different from the current one");
        }
        // If everything is ok, update the password
        found.setPassword(bcrypt.encode(body.newPassword()));
        return this.usersRepository.save(found);
    }

    // This method deletes a user by their ID
    public void findByIdAndDelete(UUID id) {
        User found = this.findById(id);
        this.usersRepository.delete(found);
    }
}
