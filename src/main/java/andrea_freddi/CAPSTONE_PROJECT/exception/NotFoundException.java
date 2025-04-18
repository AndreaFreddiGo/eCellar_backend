package andrea_freddi.CAPSTONE_PROJECT.exception;

// This class is used to handle Not Found exceptions in the application

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super(id + " not found!");
    }
}