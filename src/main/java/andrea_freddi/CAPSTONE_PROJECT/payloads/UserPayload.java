package andrea_freddi.CAPSTONE_PROJECT.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// This class represents the payload for user registration.
// It contains fields for name, surname, email, username, and password.
// The class uses annotations for field validation.

public record UserPayload(
        @NotEmpty(message = "Name cannot be empty")
        @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
        String name,
        @NotEmpty(message = "Surname cannot be empty")
        @Size(min = 2, max = 20, message = "Surname must be between 2 and 20 characters")
        String surname,
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,
        @NotEmpty(message = "Username cannot be empty")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters and include one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String username,
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password
) {
}
