package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.entities.CellarWine;
import andrea_freddi.eCellar_backend.entities.ProposalStatus;
import andrea_freddi.eCellar_backend.entities.PurchaseProposal;
import andrea_freddi.eCellar_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseProposalsRepository extends JpaRepository<PurchaseProposal, UUID> {
    // This method retrieves a list of PurchaseProposal entities associated with a specific user
    List<PurchaseProposal> findAllByUser(User user);

    // This method retrieves a list of PurchaseProposal entities associated with a specific CellarWine
    List<PurchaseProposal> findByCellarWine(CellarWine cellarWine);

    // This method retrieves a list of PurchaseProposal entities associated with a specific CellarWine and ProposalStatus
    List<PurchaseProposal> findByCellarWineAndStatus(CellarWine cellarWine, ProposalStatus status);
}
