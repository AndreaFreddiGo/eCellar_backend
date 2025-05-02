package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    // This method is used to find a user by their email address
    // It returns an Optional<User> to handle the case where the user may not exist
    Optional<User> findByEmail(String email);

    // This method is used to find a user by their username
    // It returns an Optional<User> to handle the case where the user may not exist
    Optional<User> findByUsername(String username);
}
