package id.ac.its.izzulhaq.tictactoe;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String username;
    private String hostname;
    private int port;

    public Client(String username, String hostname, int port) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            System.out.println("Connecting...");
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to " + hostname + " in port " + port);

            WriteThread write = new WriteThread(socket, this);
            write.start();

            ReadThread read = new ReadThread(socket, this);
            read.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
