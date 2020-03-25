package BankServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Bank.BankMessage;
import Bank.SensitiveAccount;
import bankExceptions.*;

public class RequestHandler extends Thread {
    private final Socket client;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private BankMessage msg;
    private final AtomicInteger counterOwner;
    private final AtomicInteger counterDest;

    private static final Lock sharedLock = new ReentrantLock();

    public RequestHandler(Socket client, ObjectInputStream in, ObjectOutputStream out, BankMessage message, AtomicInteger counterOwner, AtomicInteger counterDest) {
        this.client = client;
        this.in = in;
        this.out = out;
        this.msg = message;
        this.counterOwner = counterOwner;
        this.counterDest = counterDest;
    }

    private SensitiveAccount createAccount()  throws NoArgumentMatchException, IOException {
        if (msg.getOwnedPass() == null) {
            throw new NoArgumentMatchException();
        }

        return new SensitiveAccount(msg.getOwnedPass());
    }

    private SensitiveAccount login() throws NoArgumentMatchException, AccountDoesNotExistException, IOException, WrongPasswordException {
        if (msg.getDest() == null || msg.getOwnedPass() == null) {
            throw new NoArgumentMatchException();
        }

        return new SensitiveAccount(msg.getDest().getId(), msg.getDest().getAgency(), msg.getOwnedPass());
    }

    private void deposit() throws NoArgumentMatchException, IOException {
        if (msg.getOwned() == null || msg.getAmount() == null) {
            throw new NoArgumentMatchException();
        }

        msg.getOwned().deposit(msg.getAmount());
    }

    private void withdraw() throws NoArgumentMatchException, NotEnoughBalanceException, IOException {
        if (msg.getOwned() == null || msg.getAmount() == null) {
            throw new NoArgumentMatchException();
        }

        msg.getOwned().withdraw(msg.getAmount());
    }

    private void transfer() throws NoArgumentMatchException, AccountDoesNotExistException, NotEnoughBalanceException, IOException {
        if (msg.getOwned() == null || msg.getDest() == null || msg.getAmount() == null) {
            throw new NoArgumentMatchException();
        }

        msg.getOwned().transfer((SensitiveAccount)msg.getDest(), msg.getAmount());
    }

    private void update() throws NoArgumentMatchException, AccountDoesNotExistException, IOException, WrongPasswordException {
        if (msg.getOwned() == null) {
            throw new NoArgumentMatchException();
        }

        msg.getOwned().update();
    }

    public void run() {
        try {
            try {
                BankMessage response = null;
                switch (msg.getMessage()) {
                    case "CreateAccount": {
                        response = new BankMessage("Success", createAccount(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), msg.getExpt());
                        break;
                    }
                    case "Login": {
                        if (msg.getDest() != null) {
                            synchronized (msg.getDest()) {
                                response = new BankMessage("Success", login(), null, msg.getAmount(), msg.getDest(), msg.getExpt());
                            }
                        }
                        else {
                            throw new NoArgumentMatchException();
                        }
                        break;
                    }
                    case "Deposit": {
                        if (msg.getOwned() != null) {
                            synchronized (msg.getOwned()) {
                                msg.getOwned().update();
                                deposit();
                                response = new BankMessage("Success", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), null);
                            }
                        }
                        else {
                            throw new NoArgumentMatchException();
                        }
                        break;
                    }
                    case "Withdraw": {
                        if (msg.getOwned() != null) {
                            synchronized (msg.getOwned()) {
                                msg.getOwned().update();
                                withdraw();
                                response = new BankMessage("Success", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), null);
                            }
                        }
                        else {
                            throw new NoArgumentMatchException();
                        }
                        break;
                    }
                    case "Transfer": {
                        if (msg.getOwned() != null && msg.getDest() != null) {
                            sharedLock.lock();
                            synchronized (msg.getOwned()) {
                                synchronized (msg.getDest()) {
                                    sharedLock.unlock();

                                    msg.getOwned().update();
                                    ((SensitiveAccount)msg.getDest()).update();
                                    transfer();
                                    response = new BankMessage("Success", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), null);
                                }
                            }
                        }
                        else {
                            throw new NoArgumentMatchException();
                        }
                        break;
                    }
                    case "Update": {
                        if (msg.getOwned() != null) {
                            synchronized (msg.getOwned()) {
                                update();
                                response = new BankMessage("Success", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), null);
                            }
                        }
                        else {
                            throw new NoArgumentMatchException();
                        }
                        break;
                    }
                }

                if (response != null) {
                    out.writeObject(response);
                }
                else {
                    throw new RuntimeException(new NullPointerException().toString() + " (This should be impossible, contact your developer)");
                }
            }
            catch (Exception e) {
                out.writeObject(new BankMessage("Exception", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), e));
            }
            finally {
                out.flush();
                out.close();
                in.close();
            }
        } catch (IOException e) {
            System.out.println("IO ERROR");
        } finally {
            try {
                client.close();
            } catch (IOException e) {}
            if (counterOwner != null)
                counterOwner.decrementAndGet();
            if (counterDest != null)
                counterDest.decrementAndGet();
        }
    }
}
