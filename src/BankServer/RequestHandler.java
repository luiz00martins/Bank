package BankServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import bankExceptions.*;

public class RequestHandler extends Thread {
    private Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    private void writeFile(String fileName, String written) throws IOException {
        FileWriter file = new FileWriter("User Data/" + fileName);

        file.write(written);

        file.close();
    }

    private String readFile(String fileName) throws IOException {
        FileReader file = new FileReader("User Data/" + fileName);
        StringBuilder str = new StringBuilder();

        // Copying the whole file to str
        char c = (char)file.read();
        while (c != (char)-1) {
            str.append(c);
            c = (char)file.read();
        }

        file.close();

        return str.toString();
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            try {
                String[] args = in.readUTF().split(";");

                if (args[0].equals("CreateAccount")) {
                    if (args.length != 3) {
                        throw new NoArgumentMatchException();
                    }

                    String accountName = args[1];
                    String accountPass = args[2];

                    writeFile(accountName, accountName + "\n" + accountPass);

                } else {
                    throw new NoCommandException();
                }

                out.writeUTF("0");
            }
            catch (BankException e){
                out.writeUTF(e.toString());
            }
            catch (Exception e){
                System.out.println("free exception");
            }
            finally {
                    out.close();
                    in.close();
                    client.close();
            }
        }
        catch (IOException e){
            System.out.println("IO ERROR");
        }
    }
}
