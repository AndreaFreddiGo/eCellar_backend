package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.entities.Cellar;
import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CellarsRepository extends JpaRepository<Cellar, UUID> {
    // This method is used to find all cellars by user
    List<Cellar> findAllByUser(User user);

    // This method is used to find a cellar by its name and user
    Optional<Cellar> findByNameAndUser(String name, User user);

    // This method is used to find a cellar by its id and user
    Optional<Cellar> findByIdAndUser(UUID id, User user);

}