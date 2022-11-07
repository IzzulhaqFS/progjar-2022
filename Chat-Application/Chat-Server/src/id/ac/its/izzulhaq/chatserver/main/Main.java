package id.ac.its.izzulhaq.chatserver.main;

import id.ac.its.izzulhaq.chatserver.server.ChatServer;

public class Main {
    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("Syntax: java ChatServer <port-number>");
//            System.exit(0);
//        }

        int port = 6666;

        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
