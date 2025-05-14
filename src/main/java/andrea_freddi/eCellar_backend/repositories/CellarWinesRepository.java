package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.entities.Cellar;
import andrea_freddi.eCellar_backend.entities.CellarWine;
import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CellarWinesRepository extends JpaRepository<CellarWine, UUID> {
    // This method is used to find all CellarWines by a specific User
    List<CellarWine> findAllByUser(User user);

    List<CellarWine> findAllByCellar(Cellar cellar);
}
