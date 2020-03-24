package bankExceptions;

public class WrongPasswordException extends BankException {
    public WrongPasswordException() {
        super();
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}