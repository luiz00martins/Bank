package BankClient;

import Bank.*;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread  {

    private BankMessage msg;
    private BankMessage response;

    public ClientThread(){
        response = null;
        msg = null;
    }

    public ClientThread createAccount(Password pass){
        msg = new BankMessage("CreateAccount", null, pass, null, null, null);
        return this;
    }

    public ClientThread login(ID id, Agency agency, Password pass){
        msg = new BankMessage("Login", null, pass, null, new Account(id, agency), null);
        return this;
    }

    public ClientThread deposit(SensitiveAccount acc, Balance amount){
        msg = new BankMessage("Deposit", acc, null, amount, null, null);
        return this;
    }

    public ClientThread withdraw(SensitiveAccount acc, Balance amount){
        msg = new BankMessage("Withdraw", acc, null, amount, null, null);
        return this;
    }

    public ClientThread transfer(SensitiveAccount accSend, Account accRevc, Balance amount){
        msg = new BankMessage("Transfer", accSend, null, amount, accRevc, null);
        return this;
    }

    public void run() {
        if (!msg.getMessage().equals("")){
            try {
                Socket clientSocket = new Socket("127.0.0.1", 12345);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                out.writeObject(msg);
                response = (BankMessage)in.readObject();

                in.close();
                out.close();
                clientSocket.close();
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        }

    }

    public BankMessage getResponse() {
        return response;
    }
}
