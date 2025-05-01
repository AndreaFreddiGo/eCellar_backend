package andrea_freddi.CAPSTONE_PROJECT.mappers;

import andrea_freddi.CAPSTONE_PROJECT.entities.PurchaseProposal;
import andrea_freddi.CAPSTONE_PROJECT.payloads.PurchaseProposalDTO;
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
