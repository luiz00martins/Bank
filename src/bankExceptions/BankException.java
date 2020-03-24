package bankExceptions;

public abstract class BankException extends Exception {

    public BankException() {
        super();
    }

    public BankException(String message) {
        super(message);
    }
}
