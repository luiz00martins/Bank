package bankExceptions;

public class AccountDoesNotExistException extends BankException  {
    public AccountDoesNotExistException() {
        super();
    }

    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
