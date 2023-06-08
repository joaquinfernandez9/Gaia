package domain.error;

public class AmbiguousRequestException extends RuntimeException{
    public AmbiguousRequestException(String message) {
        super(message);
    }
}
