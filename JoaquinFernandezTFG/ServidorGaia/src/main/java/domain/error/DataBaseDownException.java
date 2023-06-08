package domain.error;

public class DataBaseDownException extends RuntimeException{
    public DataBaseDownException(String msg) {
        super(msg);
    }
}
