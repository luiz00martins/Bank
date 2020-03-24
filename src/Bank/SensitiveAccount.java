package Bank;

import bankExceptions.AccountDoesNotExistException;
import bankExceptions.NotEnoughBalanceException;
import bankExceptions.WrongPasswordException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SensitiveAccount extends Account {
    private Password password;
    Balance balance;

    public SensitiveAccount(Password pass) throws IOException {
        do {
            Random rand = new Random();

            this.id = new ID(String.format("%05d", rand.nextInt(99999)) + "-" + rand.nextInt(9));
            this.agency = new Agency(String.format("%04d", rand.nextInt(9999)) + "-" + rand.nextInt(9));

            this.password = pass;
            this.balance = new Balance(0, 0);
        } while (exists());

        save();
    }

    public SensitiveAccount(ID id, Agency agency, Password pass) throws AccountDoesNotExistException, IOException, WrongPasswordException {
        FileReader file;
        try {
            file = new FileReader("User Data/" + id + " " + agency);
        }
        catch (IOException e) {
            throw new AccountDoesNotExistException();
        }

        StringBuilder str = new StringBuilder();

        // Copying the whole file to str
        char c = (char)file.read();
        while (c != (char)-1) {
            str.append(c);
            c = (char)file.read();
        }

        String[] data = str.toString().split("\n");

        if (!data[2].equals(pass.toString())) {
            file.close();
            throw new WrongPasswordException();
        }

        this.id = new ID(data[0]);
        this.agency = new Agency(data[1]);
        this.password = new Password(data[2]);
        this.balance = new Balance(data[3]);

        file.close();
    }

    public synchronized void deposit(Balance amount) throws IOException {
        balance.add(amount);

        try {
            save();
        }
        catch (IOException e) {
            try {
                balance.sub(amount);
            }
            catch (NotEnoughBalanceException ex) {
                throw new RuntimeException(ex.toString() + " (This should be impossible, contact your developer)");
            }

            throw e;
        }
    }

    public void deposit(int dollars, int cents) throws IOException {
        deposit(new Balance(dollars, cents));
    }

    public synchronized void withdraw(Balance amount) throws NotEnoughBalanceException, IOException {
        balance.sub(amount);

        try {
            save();
        }
        catch (IOException e) {
            balance.add(amount);
            throw e;
        }
    }

    public void withdraw(int dollars, int cents) throws NotEnoughBalanceException, IOException  {
        withdraw(new Balance(dollars, cents));
    }

    public synchronized void transfer(SensitiveAccount acc, Balance amount) throws AccountDoesNotExistException, NotEnoughBalanceException, IOException {
        if (acc.exists()) {
            balance.sub(amount);
            acc.balance.add(amount);

            // Trying to save
            try {
                save();
                acc.save();
            }
            catch (IOException e) {
                // Unoing work
                balance.add(amount);
                try {
                    acc.balance.sub(amount);
                }
                catch (NotEnoughBalanceException ex) {
                    throw new RuntimeException(e.toString() + " (This should be impossible, contact your developer)");
                }
                // Throwing
                throw e;
            }
        }
        else {
            throw new AccountDoesNotExistException();
        }
    }

    public void transfer(SensitiveAccount acc, int dollars, int cents) throws AccountDoesNotExistException, NotEnoughBalanceException, IOException {
        transfer(acc, new Balance(dollars, cents));
    }
    public synchronized Account toAccount() {
        return new Account(new ID(id.toString()), new Agency(agency.toString()));
    }

    public synchronized void save() throws IOException {
        FileWriter file = new FileWriter("User Data/" + id + " " + agency);

        file.write(id + "\n");
        file.append(agency + "\n");
        file.append(password + "\n");
        file.append(balance.toString());

        file.close();
    }

    public static synchronized SensitiveAccount load(Account acc) throws AccountDoesNotExistException, IOException {
        FileReader file;
        try {
            file = new FileReader("User Data/" + acc.id + " " + acc.agency);
        }
        catch (IOException e) {
            throw new AccountDoesNotExistException();
        }

        StringBuilder str = new StringBuilder();

        // Copying the whole file to str
        char c = (char)file.read();
        while (c != (char)-1) {
            str.append(c);
            c = (char)file.read();
        }

        file.close();

        String[] data = str.toString().split("\n");

        try {
            return new SensitiveAccount(new ID(data[0]), new Agency(data[1]), new Password(data[2]));
        }
        catch (WrongPasswordException e) {
            throw new RuntimeException(e.toString() + " (This should be impossible, contact your developer)");
        }
    }

    public Balance getBalance() {
        return balance;
    }
}
