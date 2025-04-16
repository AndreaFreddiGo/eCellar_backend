package andrea_freddi.CAPSTONE_PROJECT.exception;

// creo una classe di eccezione personalizzata per gestire gli errori 400

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
