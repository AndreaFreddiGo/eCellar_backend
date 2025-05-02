package andrea_freddi.eCellar_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * This class is used to receive the data from the client
 * It contains the fields that are required for the cellar
 * The fields are validated using Jakarta Bean Validation annotations
 */

public record CellarPayload(

        @NotBlank(message = "Cellar name cannot be blank")
        @Size(min = 2, max = 50, message = "Cellar name must be between 2 and 50 characters")
        String name,

        @Size(max = 500, message = "Description is too long")
        String description

) {
}
