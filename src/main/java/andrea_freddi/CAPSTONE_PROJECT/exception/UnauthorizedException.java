package andrea_freddi.CAPSTONE_PROJECT.exception;

// This class represents an exception that is thrown when a user is not authorized to perform a certain action

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
