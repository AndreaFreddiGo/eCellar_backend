package andrea_freddi.CAPSTONE_PROJECT.exception;

// creo l'eccezione NotFoundException per gestire gli errori 404

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super(id + " not found!");
    }
}