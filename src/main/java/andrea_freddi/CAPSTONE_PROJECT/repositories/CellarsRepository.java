package andrea_freddi.CAPSTONE_PROJECT.repositories;

import andrea_freddi.CAPSTONE_PROJECT.entities.Cellar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CellarsRepository extends JpaRepository<Cellar, UUID> {
    // This method is used to find all cellars by user ID
    Page<Cellar> findAllByUserId(UUID userId, Pageable pageable);

    // This method is used to find a cellar by its name and user ID
    Optional<Cellar> findByNameAndUserId(String name, UUID userId);

}