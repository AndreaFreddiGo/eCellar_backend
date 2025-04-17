package andrea_freddi.CAPSTONE_PROJECT.payloads;


import andrea_freddi.CAPSTONE_PROJECT.entities.BottleSize;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CellarWinePayload(

        @NotNull(message = "Wine ID is required")
        UUID wineId,

        @NotNull(message = "Cellar ID is required")
        UUID cellarId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotNull(message = "Bottle size must be specified")
        BottleSize bottleSize,

        @NotNull(message = "Public visibility must be specified")
        Boolean isPublic,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
        BigDecimal askingPrice,

        @Size(max = 1000, message = "Notes are too long")
        String personalNotes,

        @Min(value = 1900, message = "Purchase year must be valid")
        @Max(value = 2100, message = "Purchase year must be valid")
        Integer purchaseYear,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 100, message = "Rating cannot exceed 100")
        Integer personalRating
) {
}
