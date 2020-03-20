package BankServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    void test(){

    }

    public static void main(String[] args) throws java.io.IOException {
        Account myAccount = new Account();
        myAccount.deposit(10, 20);

        ServerSocket serverSocket = new ServerSocket(12345);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while(true){
            Socket client = serverSocket.accept();

            executorService.execute(new RequestHandler(client));
        }

        /*ServerThread server = new ServerThread();
        server.start();
        ClientThread client = new ClientThread();
        client.start();*/
        //return;
    }
}
