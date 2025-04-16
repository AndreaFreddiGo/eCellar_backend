package andrea_freddi.CAPSTONE_PROJECT.exception;

// creo una nuova eccezione per gestire gli errori di autenticazione 401

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
