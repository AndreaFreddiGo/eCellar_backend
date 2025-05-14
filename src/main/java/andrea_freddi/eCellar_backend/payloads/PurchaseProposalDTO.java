package andrea_freddi.eCellar_backend.payloads;

import andrea_freddi.eCellar_backend.entities.ProposalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * This DTO represents the purchase proposal data returned to the client,
 * including the related wine info (CellarWineDTO).
 */

public record PurchaseProposalDTO(

        UUID id,
        UUID userId,
        UUID cellarWineId,
        BigDecimal proposingPrice,
        ProposalStatus status,
        LocalDateTime proposalDate,
        String message,
        CellarWineDTO wine
) {
}
