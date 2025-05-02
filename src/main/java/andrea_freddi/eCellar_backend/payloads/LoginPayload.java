package andrea_freddi.eCellar_backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * This class is used to receive the login data from the client
 * It contains the fields that are required for login
 * The fields are validated using Jakarta Bean Validation annotations
 */

public record LoginPayload(

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password

) {
}

