package Bank;

import bankExceptions.NotEnoughBalanceException;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Balance implements Serializable {
    private int dollars;
    private int cents;

    public Balance(int dollars, int cents) {
        if (dollars < 0 || cents < 0) {
            throw new IllegalArgumentException();
        }

        this.dollars = dollars;
        this.cents = cents;

        while (this.cents >= 100) {
            this.cents -= 100;
            this.dollars++;
        }
    }

    public Balance(String balance) {
        String[] parts = balance.split(",");

        // Checking format
        if (parts.length != 2)
            throw new IllegalArgumentException("Illegal balance format");

        // Checking if they are all numbers
        for (int i = 0; i < parts[0].length(); i++) {
            if (!Character.isDigit(parts[0].charAt(i)))
                throw new IllegalArgumentException("Only digits are accepted in Balance");
        }
        for (int i = 0; i < parts[1].length(); i++) {
            if (!Character.isDigit(parts[1].charAt(i)))
                throw new IllegalArgumentException("Only digits are accepted in Balance");
        }

        this.dollars = Integer.parseInt(parts[0]);
        this.cents = Integer.parseInt(parts[1]);
    }

    // Note: this may be a dangerous implementation of synchronization

    public synchronized void add(int dollars, int cents) {
        this.dollars += dollars;
        this.cents += cents;

        while (this.cents >= 100) {
            this.cents -= 100;
            this.dollars++;
        }
    }

    public void add(Balance amount) {
        add(amount.dollars, amount.cents);
    }

    public synchronized void sub(int dollars, int cents) throws NotEnoughBalanceException {
        if (this.dollars > dollars || (this.dollars == cents && this.cents >= cents)) {
            this.dollars -= dollars;
            this.cents -= cents;

            if (this.cents < 0) {
                this.cents += 100;
                this.dollars--;
            }
        }
        else {
            throw new NotEnoughBalanceException();
        }
    }

    public void sub(Balance amount) throws NotEnoughBalanceException {
        sub(amount.dollars, amount.cents);
    }

    public synchronized String toString() {
        return Integer.toString(dollars) + "," + cents;
    }

    public synchronized void fromString(String str) {
        String[] vals = str.split(",");
        dollars = Integer.parseInt(vals[0]);
        cents = Integer.parseInt(vals[1]);
    }
}
