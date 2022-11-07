package id.ac.its.izzulhaq.chatclient.thread;

import id.ac.its.izzulhaq.chatclient.ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader br;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
        }
        catch (IOException e) {
            System.out.println("Error occurred on ReadThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = br.readLine();
                System.out.println("\n" + response);

                if (client.getUserName() != null) {
                    System.out.print(client.getUserName() + " : ");
                }
            }
            catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
