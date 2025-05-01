package andrea_freddi.CAPSTONE_PROJECT.payloads;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * This record is used to receive the proposal creation request from the client.
 * It includes the reference to the target cellar wine and the proposed price.
 */

public record PurchaseProposalPayload(

        @NotNull(message = "CellarWine ID is required")
        UUID cellarWineId,

        @NotNull(message = "Proposed price must be provided")
        @DecimalMin(value = "0.01", message = "Proposed price must be greater than zero")
        BigDecimal proposingPrice,

        @Size(max = 500, message = "Message must be at most 500 characters")
        String message
) {
}
