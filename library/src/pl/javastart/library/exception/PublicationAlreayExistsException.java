package pl.javastart.library.exception;

public class PublicationAlreayExistsException extends RuntimeException {
    public PublicationAlreayExistsException(String message) {
        super(message);
    }
}
