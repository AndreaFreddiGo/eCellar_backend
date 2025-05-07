package andrea_freddi.eCellar_backend.payloads;

import java.time.LocalDateTime;

// This class is used to return the error message to the client
// It contains the message and the timestamp of the error

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp) {
}