package BankClient;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread  {

    private String name;
    private String pass;

    public ClientThread(String name, String pass){
        this.name = name;
        this.pass = pass;
    }

    public void run() {
        try {
            Socket clientSocket = new Socket("127.0.0.1", 12345);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            out.writeUTF("CreateAccount;" + name + ";" + pass);
            System.out.println(in.readUTF());

            in.close();
            out.close();
            clientSocket.close();
        }
        catch (Exception e){
            System.out.println("An error happened");
        }
    }
}
