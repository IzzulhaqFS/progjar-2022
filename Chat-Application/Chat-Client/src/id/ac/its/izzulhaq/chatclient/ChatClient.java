package id.ac.its.izzulhaq.chatclient;

import id.ac.its.izzulhaq.chatclient.thread.ReadThread;
import id.ac.its.izzulhaq.chatclient.thread.WriteThread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        }
        catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostName, port);
        client.execute();
    }
}
