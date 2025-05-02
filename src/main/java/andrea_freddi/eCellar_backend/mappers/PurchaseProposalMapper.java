package andrea_freddi.eCellar_backend.mappers;

import andrea_freddi.eCellar_backend.entities.PurchaseProposal;
import andrea_freddi.eCellar_backend.payloads.PurchaseProposalDTO;
import org.springframework.stereotype.Component;


// This class is responsible for mapping between PurchaseProposal entities and DTOs


@Component
public class PurchaseProposalMapper {

    public PurchaseProposalDTO purchaseProposalDTO(PurchaseProposal proposal) {
        if (proposal == null) return null;

        return new PurchaseProposalDTO(
                proposal.getId(),
                proposal.getUser() != null ? proposal.getUser().getId() : null,
                proposal.getCellarWine() != null ? proposal.getCellarWine().getId() : null,
                proposal.getProposingPrice(),
                proposal.getStatus(),
                proposal.getProposalDate(),
                proposal.getMessage()
        );
    }
}
