package andrea_freddi.eCellar_backend.mappers;

import andrea_freddi.eCellar_backend.entities.CellarWine;
import andrea_freddi.eCellar_backend.entities.ProposalStatus;
import andrea_freddi.eCellar_backend.payloads.CellarWineDTO;
import org.springframework.stereotype.Component;

@Component
public class CellarWineMapper {

    // Converts a CellarWine entity to a CellarWineDTO
    public CellarWineDTO cellarWineToDTO(CellarWine cellarWine) {
        if (cellarWine == null) return null;

        boolean hasPendingProposal = cellarWine.getPurchaseProposals() != null &&
                cellarWine.getPurchaseProposals().stream()
                        .anyMatch(p -> p.getStatus() == ProposalStatus.PENDING);

        return new CellarWineDTO(
                cellarWine.getId(),
                cellarWine.getQuantity(),
                cellarWine.getSize(),
                cellarWine.isPublic(),
                cellarWine.getPersonalNotes(),
                cellarWine.getPurchaseDate(),
                cellarWine.getPurchasePrice(),
                cellarWine.getAskingPrice(),
                cellarWine.getMyScore(),
                cellarWine.getUser().getId(),
                cellarWine.getCellar() != null ? cellarWine.getCellar().getId() : null,
                cellarWine.getWine().getId(),
                cellarWine.getWine().getName(),
                cellarWine.getWine().getProducer(),
                cellarWine.getWine().getVintage(),
                cellarWine.getUser().getUsername(),
                hasPendingProposal
        );
    }
}
