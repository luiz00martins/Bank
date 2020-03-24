package bankExceptions;

public class NoCommandException extends BankException{
    public NoCommandException() {
        super();
    }

    public NoCommandException(String message) {
        super(message);
    }
}