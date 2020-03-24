package bankExceptions;

public class NotEnoughBalanceException extends BankException  {
    public NotEnoughBalanceException() {
        super();
    }

    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
