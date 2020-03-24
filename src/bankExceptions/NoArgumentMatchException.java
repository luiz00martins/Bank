package bankExceptions;

public class NoArgumentMatchException extends BankException {
    public NoArgumentMatchException() {
        super();
    }

    public NoArgumentMatchException(String message) {
        super(message);
    }
}
