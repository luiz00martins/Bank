package BankServer;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket client = serverSocket.accept();


            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            System.out.println(in.readUTF());
            out.writeUTF("Rai te fude rapa");

            out.close();
            in.close();
            client.close();
            serverSocket.close();
        }
        catch (Exception e){
            System.out.println("An error happened");
        }
    }
}
