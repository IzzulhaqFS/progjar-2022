package id.ac.its.izzulhaq.tictactoe;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private Socket socket;
    private Client client;
    private PrintWriter writer;

    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        String username = client.getUsername();
        writer.println(username);

        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();

        while (!command.equals("exit")) {
            writer.println(command);
            command = scanner.next();
        }

        scanner.close();
        writer.close();
    }
}
