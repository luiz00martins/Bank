package BankClient;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import BankClient.ClientThread;

public class Main {

    void test(){

    }

    public static void main(String[] args) {

        (new ClientThread("anAccount", "password123")).start();
        (new ClientThread("anotherAccount", "pass321")).start();
        (new ClientThread("XxAccountxX", "veryhardpassword")).start();
        (new ClientThread("Account123", "securiry101")).start();


        /*ServerThread server = new ServerThread();
        server.start();
        ClientThread client = new ClientThread();
        client.start();*/
        //return;
    }
}
