package andrea_freddi.CAPSTONE_PROJECT.payloads;

import java.time.LocalDateTime;

// creo una classe DTO per definire come mi verranno restituiti gli errori
// uso Payload per quello che devo ricevere in costruzione e DTO per quello che restituisco

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp) {
}