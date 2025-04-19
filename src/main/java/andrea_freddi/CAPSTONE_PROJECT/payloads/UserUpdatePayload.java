package andrea_freddi.CAPSTONE_PROJECT.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/*
 * UserUpdatePayload is a record that represents the payload for updating user information.
 * It contains fields for name, surname, email, username, biography, phone, birthDate,
 * profilePicture, preferredLanguage, and publicProfile.
 * Each field has validation constraints to ensure the data is valid.
 */

public record UserUpdatePayload(

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

        @Size(max = 100, message = "Biography too long")
        String biography,

        @Pattern(regexp = "^[0-9+\\s()-]{6,20}$", message = "Phone number must be valid")
        String phone,

        @Past(message = "Birthdate must be in the past")
        LocalDate birthDate,

        @Size(max = 300, message = "Profile picture URL too long")
        String profilePicture,

        @Size(min = 2, max = 10, message = "Language code should be short")
        String preferredLanguage,

        @NotNull(message = "Public profile cannot be null")
        Boolean publicProfile

) {
}

