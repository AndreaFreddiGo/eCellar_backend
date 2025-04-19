package andrea_freddi.CAPSTONE_PROJECT.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
 * UserPayload is a record class that represents the payload for user registration
 * It contains fields for name, surname, email, username, and password
 * The class uses Jakarta Bean Validation annotations to enforce validation rules on the fields
 */

public record UserPayload(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
        String name,

        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 2, max = 20, message = "Surname must be between 2 and 20 characters")
        String surname,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters and include one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password
) {
}
