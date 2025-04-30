package andrea_freddi.CAPSTONE_PROJECT.payloads;

import andrea_freddi.CAPSTONE_PROJECT.entities.*;
import jakarta.validation.constraints.*;

import java.util.List;

/*
 * WinePayload is used to receive wine data from the client.
 * It includes validation annotations to ensure input correctness.
 * Admins may also specify the wine status and scoring manually.
 */

public record WinePayload(

        @NotBlank(message = "Wine name cannot be blank")
        String name,

        @NotBlank(message = "Producer cannot be blank")
        String producer,

        @Min(value = 1900, message = "Vintage must be realistic")
        @Max(value = 2100, message = "Vintage must be realistic")
        Integer vintage,

        @DecimalMin(value = "0.0", inclusive = false, message = "ABV must be positive")
        @DecimalMax(value = "25.0", message = "ABV must be realistic")
        Float abv,

        @NotEmpty(message = "At least one grape variety is required")
        List<@NotBlank(message = "Grape variety cannot be blank") String> grapeVarieties,

        @Size(max = 255, message = "Appellation is too long")
        String appellation,

        @NotBlank(message = "Country cannot be blank")
        String country,

        @Size(max = 255, message = "Region is too long")
        String region,

        @NotNull(message = "Color is required")
        WineColor color,

        WineSweetness sweetness,
        WineEffervescence effervescence,
        WineCategory category,

        @Size(max = 1000, message = "Description is too long")
        String description,

        @Size(max = 500, message = "Image URL is too long")
        String imageUrl,

        @Min(value = 1900, message = "Begin consume year must be valid")
        @Max(value = 2100, message = "Begin consume year must be valid")
        Integer beginConsumeYear,

        @Min(value = 1900, message = "End consume year must be valid")
        @Max(value = 2100, message = "End consume year must be valid")
        Integer endConsumeYear,

        Drinkability drinkability,

        @Size(max = 50, message = "Barcode must be shorter")
        String barcode,

        // Optional scoring fields (typically set by admins)
        @DecimalMin(value = "0.0", inclusive = false, message = "Professional score must be positive")
        @DecimalMax(value = "100.0", message = "Professional score must be realistic")
        Float professionalScore,

        @DecimalMin(value = "0.0", inclusive = false, message = "Community score must be positive")
        @DecimalMax(value = "100.0", message = "Community score must be realistic")
        Float communityScore,

        // Optional field - only admins should set this
        WineStatus status
) {
}
