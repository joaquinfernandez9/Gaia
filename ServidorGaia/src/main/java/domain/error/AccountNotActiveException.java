package domain.error;

public class AccountNotActiveException extends RuntimeException{
    public AccountNotActiveException(String msg) {
        super(msg);
    }
}
