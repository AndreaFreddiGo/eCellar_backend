package andrea_freddi.eCellar_backend.payloads;

// This class is used to return the token to the client after login
// I use Payload for what I need to receive in construction and DTO for what I return

import java.util.UUID;

public record LoginDTO(
        String token,
        String name,
        String username,
        UUID userId,
        String profilePicture
) {
}
