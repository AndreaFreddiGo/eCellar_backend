package andrea_freddi.eCellar_backend.payloads;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// This class is used to return the user data to the client
// I use Payload for what I need to receive in construction and DTO for what I return

public record UserDTO(
        UUID id,
        String name,
        String surname,
        String email,
        String username,
        String biography,
        String phone,
        LocalDate birthDate,
        String profilePicture,
        String preferredLanguage,
        boolean publicProfile,
        LocalDateTime registrationDate
) {
}
