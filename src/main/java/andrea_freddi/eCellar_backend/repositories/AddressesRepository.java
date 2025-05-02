package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.entities.Address;
import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressesRepository extends JpaRepository<Address, UUID> {
    // This method retrieves a list of addresses associated with a specific user
    List<Address> findAllByUser(User user);

    // This method retrieves an address by its label and the user it belongs to
    Optional<Address> findByLabelAndUser(String label, User user);

    // This method is used to find an address by its ID and the user it belongs to
    Optional<Address> findByIdAndUser(UUID id, User user);

}
