package andrea_freddi.eCellar_backend.payloads;


import andrea_freddi.eCellar_backend.entities.BottleSize;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * This class is used to receive the data from the client when adding a wine to the cellar.
 * It contains all the fields that are needed to create a new CellarWine entity.
 * The validation annotations are used to validate the data received from the client.
 */

public record CellarWinePayload(

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotNull(message = "Bottle size must be specified")
        BottleSize size,

        @NotNull(message = "Public visibility must be specified")
        Boolean isPublic,

        @Size(max = 1000, message = "Notes are too long")
        String personalNotes,

        @Min(value = 1900, message = "Purchase year must be valid")
        @Max(value = 2100, message = "Purchase year must be valid")
        Integer purchaseDate,

        @DecimalMin(value = "0.0", inclusive = false, message = "Purchase price must be positive")
        BigDecimal purchasePrice,

        @DecimalMin(value = "0.0", inclusive = false, message = "Asking price must be positive")
        BigDecimal askingPrice,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 100, message = "Rating cannot exceed 100")
        Integer myScore,

        @NotNull(message = "Wine ID is required")
        UUID wineId,

        @NotNull(message = "Cellar ID is required")
        UUID cellarId
) {
}
