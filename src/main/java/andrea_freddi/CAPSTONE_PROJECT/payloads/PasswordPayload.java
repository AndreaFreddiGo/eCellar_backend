package andrea_freddi.CAPSTONE_PROJECT.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// This class is used to create a payload for the password change request

public record PasswordPayload(

        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "New password must be at least 8 characters and include one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String newPassword

) {
}
