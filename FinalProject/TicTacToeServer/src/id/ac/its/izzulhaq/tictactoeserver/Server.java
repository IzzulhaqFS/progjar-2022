package id.ac.its.izzulhaq.tictactoeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private int port;
    private HashMap<PlayerThread, String> players = new HashMap<>();
    private ArrayList<RoomThread> rooms = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void execute() {
        try {
            System.out.println("Starting server...");
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server listening in port " + port);

            while (true) {
                Socket socket = server.accept();
                System.out.println("Player connected");

                PlayerThread newPlayer = new PlayerThread(socket, this);
                newPlayer.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(PlayerThread player, String username) {
        players.put(player, username);
    }

    public void deletePlayer(PlayerThread player) {
        players.remove(player);
    }

    public int roomCount() {
        return rooms.size();
    }

    public void createRoom(PlayerThread player) {
        RoomThread room = new RoomThread(player);
        room.start();
        rooms.add(room);
    }

    public RoomThread getRoom() {
        for (RoomThread room : rooms) {
            if (isRoomAvailable(room)) {
                return room;
            }
        }
        return null;
    }

    public boolean isRoomAvailable(RoomThread room) {
        return room.isAvailable();
    }

    public void joinRoom(PlayerThread player, RoomThread room) {
        room.setPlayer2(player);
    }

    public void deleteRoom(RoomThread room) {
        rooms.remove(room);
    }
}
