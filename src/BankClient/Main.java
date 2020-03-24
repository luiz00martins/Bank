package BankClient;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Bank.*;
import BankClient.ClientThread;

public class Main {

    static void testException(BankMessage message) throws Exception {
        if (message.getMessage().equals("Exception")) {
            throw message.getExpt();
        }
    }

    public static SensitiveAccount tryCreateAccount(Password pass) throws Exception {
        ClientThread t = new ClientThread().createAccount(pass);
        t.start();
        t.join();

        // Checking if it logged in successfully
        testException(t.getResponse());

        // If yes, getting logged account
        return t.getResponse().getOwned();
    }

    public static SensitiveAccount tryLogin(ID id, Agency agency, Password pass) throws Exception {
        ClientThread t = new ClientThread().login(id, agency, pass);
        t.start();
        t.join();

        // Checking if it logged in successfully
        testException(t.getResponse());

        // If yes, getting logged account
        return t.getResponse().getOwned();
    }

    public static SensitiveAccount tryDeposit(SensitiveAccount acc, Balance amount) throws Exception {
        ClientThread t = new ClientThread().deposit(acc, amount);
        t.start();
        t.join();

        // Checking if it logged in successfully
        testException(t.getResponse());

        // If yes, getting logged account
        return t.getResponse().getOwned();
    }

    public static SensitiveAccount tryWithdraw(SensitiveAccount acc, Balance amount) throws Exception {
        ClientThread t = new ClientThread().withdraw(acc, amount);
        t.start();
        t.join();

        // Checking if it logged in successfully
        testException(t.getResponse());

        // If yes, getting logged account
        return t.getResponse().getOwned();
    }

    public static SensitiveAccount tryTransfer(SensitiveAccount accSend, Account accRevc, Balance amount) throws Exception {
        ClientThread t = new ClientThread().transfer(accSend, accRevc, amount);
        t.start();
        t.join();

        // Checking if it logged in successfully
        testException(t.getResponse());

        // If yes, getting logged account
        return t.getResponse().getOwned();
    }

    public static void main(String[] args) {
        SensitiveAccount acc = null;

        try{
            acc = tryLogin(new ID("90619-6"), new Agency("2655-1"), new Password("4321"));

            acc = tryTransfer(acc, new Account(new ID("30596-7"), new Agency("0187-8")), new Balance(100, 0));




            acc = tryCreateAccount(new Password("4321"));
            acc = tryDeposit(acc, new Balance(400, 25));

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }



        /*ServerThread server = new ServerThread();
        server.start();
        ClientThread client = new ClientThread();
        client.start();*/
        //return;
        return;
    }
}
