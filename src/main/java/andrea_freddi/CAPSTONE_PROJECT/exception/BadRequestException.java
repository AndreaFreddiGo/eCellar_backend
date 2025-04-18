package andrea_freddi.CAPSTONE_PROJECT.exception;

// This class represents a custom exception that is thrown when a bad request is made

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
