package id.ac.its.izzulhaq.tictactoeserver;

import java.io.*;
import java.net.Socket;

public class PlayerThread extends Thread {
    private String username;
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;

    public PlayerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);

            username = reader.readLine();
            server.addPlayer(this, username);
            System.out.println("Connected player " + username);

            String menu = "Welcome to TicTacToe Online " + username + "\n" +
                    "Type \"play\" to play the game\n" +
                    "Type \"exit\" to exit from the game";
            writer.println(menu);

            String command = reader.readLine();
            while (!command.equals("exit")) {
                if (command.equals("play")) {
                    if (server.roomCount() < 1) {
                        server.createRoom(this);
                    }
                    else {
                        RoomThread room = server.getRoom();
                        if (room == null) {
                            server.createRoom(this);
                        }
                        else {
                            server.joinRoom(this, room);
                        }
                    }
                }
            }

            reader.close();
            writer.close();
            socket.close();
            server.deletePlayer(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String receiveMessage() {
        try {
            return reader.readLine();
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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
