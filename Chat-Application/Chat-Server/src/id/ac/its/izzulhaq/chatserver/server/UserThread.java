package id.ac.its.izzulhaq.chatserver.server;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = br.readLine();
            server.addUserName(userName);

            String serverMessage = "User connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = br.readLine();
                serverMessage = userName + " : " + clientMessage;
                server.broadcast(serverMessage, this);
            }
            while (!clientMessage.equals("exit"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quit from chat";
            server.broadcast(serverMessage, this);
        }
        catch (IOException e) {
            System.out.println("Error occurred in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        }
        else {
            writer.println("No other user connected");
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
