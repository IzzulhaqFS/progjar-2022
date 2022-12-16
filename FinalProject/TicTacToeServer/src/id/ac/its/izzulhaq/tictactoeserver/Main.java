package id.ac.its.izzulhaq.tictactoeserver;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(6666);
        server.execute();
    }
}
