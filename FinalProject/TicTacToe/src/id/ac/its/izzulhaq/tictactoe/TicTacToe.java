package id.ac.its.izzulhaq.tictactoe;

import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username");
        String username = scanner.next();
        System.out.println("Username: " + username);

        Client client = new Client(username, "localhost", 6666);
        client.execute();
    }
}
