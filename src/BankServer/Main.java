package BankServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import Bank.Account;
import Bank.BankMessage;
import Bank.SensitiveAccount;
import bankExceptions.AccountDoesNotExistException;

public class Main {
    private static ServerSocket serverSocket;
    private static List<SensitiveAccount> beingUsed;
    private static List<AtomicInteger> waitingQueues;


    /**
     * @param acc account to be checked
     * @return The {@code Account} position on List {@code beingUsed} if it's there, {@code -1} otherwise
     */
    private static int findUsed(Account acc) {
        for (int i = 0; i < beingUsed.size(); i++){
            // If one is found, get its lock and add it to the count
            if (acc.equals(beingUsed.get(i))){
                return i;
            }
        }
        return -1;
    }

    private static void removeClosed() {
        // Closing owned accounts that are not being used
        for (int i = 0; i < beingUsed.size(); i++) {
            if (waitingQueues.get(i).get() == 0) {
                waitingQueues.remove(i);
                beingUsed.remove(i);
                i--;
            }
        }
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(12345);
            beingUsed = new ArrayList<>(0);
            waitingQueues = new ArrayList<>(0);

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while(true) {
                // Trying to establish connection
                try {
                    // Waiting for connection
                    Socket client = serverSocket.accept();

                    // Establishing stream
                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                    // Reading request
                    BankMessage msg = (BankMessage)in.readObject();

                    // Trying to verify account
                    try {
                        if (msg.getDest() != null) {
                            msg = new BankMessage(msg.getMessage(), msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), SensitiveAccount.load(msg.getDest()), msg.getExpt());
                        }

                        // Adding accounts to queue
                        AtomicInteger counterOwned = null;
                        SensitiveAccount owned = msg.getOwned();
                        // Adding possible owned account to its queue
                        if (msg.getOwned() != null) {
                            // Try to see if the account is being used
                            int pos = findUsed(msg.getOwned());
                            // If is not being used, add to the used accounts
                            if (pos == -1) {
                                beingUsed.add(owned);
                                counterOwned = new AtomicInteger(1);
                                waitingQueues.add(counterOwned);
                            }
                            // If it's being used, get the account being used
                            else {
                                owned = beingUsed.get(pos);
                                waitingQueues.get(pos).incrementAndGet();
                                counterOwned = waitingQueues.get(pos);
                            }
                        }

                        AtomicInteger counterDest = null;
                        SensitiveAccount dest = null;
                        // Adding possible destination account to its queue
                        if (msg.getDest() != null) {
                            dest = SensitiveAccount.load(msg.getDest());
                            // Try to see if the account is being used
                            int pos = findUsed(msg.getDest());
                            // If is not being used, add to the used accounts
                            if (pos == -1) {
                                beingUsed.add(dest);
                                counterDest = new AtomicInteger(1);
                                waitingQueues.add(counterDest);
                            }
                            // If it's being used, get the account being used
                            else {
                                dest = beingUsed.get(pos);
                                waitingQueues.get(pos).incrementAndGet();
                                counterDest = waitingQueues.get(pos);
                            }
                        }

                        BankMessage newMessage = new BankMessage(msg.getMessage(), owned, msg.getOwnedPass(), msg.getAmount(), dest, msg.getExpt());
                        executorService.execute(new RequestHandler(client, out, newMessage, counterOwned, counterDest));

                        removeClosed();
                    } catch (AccountDoesNotExistException e) {
                        out.writeObject(new BankMessage("Exception", msg.getOwned(), msg.getOwnedPass(), msg.getAmount(), msg.getDest(), e));
                        out.close();
                        in.close();
                    }
                }
                catch (IOException e) {
                    System.out.println("Connection error");
                }
            }
        }
        catch (IOException e) {
            System.out.println("Connection error");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found, could not unmarshall");
        }


        /*ServerThread server = new ServerThread();
        server.start();
        ClientThread client = new ClientThread();
        client.start();*/
        //return;
    }
}
