package id.ac.its.izzulhaq.multiclientwebserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        ServerSocket server = null;

        try {
            Properties prop =new Properties();
            File configFile = new File("config.conf");

            FileInputStream inputStream = new FileInputStream(configFile);
            prop.load(inputStream);

            String ip = prop.getProperty("ip");
            int port = Integer.parseInt(prop.getProperty("port"));

            InetAddress address =InetAddress.getByName(ip);

            server = new ServerSocket(port, 5, address);

            System.out.println("Server Started...");

            server.setReuseAddress(true);

            while (true) {
                Socket client = server.accept();

                String hostname = client.getInetAddress().getHostName();

                System.out.println("Client Connected " + client.getInetAddress().getHostAddress());

                String rootDir;
                String dir1 = prop.getProperty("direktori1");
                String dir2 = prop.getProperty("direktori2");

                if (hostname.equals(dir1)) {
                    rootDir = dir1;
                    System.out.println("Root Directory " + rootDir);
                }
                else if (hostname.equals(dir2)) {
                    rootDir = dir2;
                    System.out.println("Root Directory " + rootDir);
                }
                else {
                    rootDir = dir1;
                    System.out.println("Root Directory " + rootDir);
                }

                ClientHandler handler = new ClientHandler(client, rootDir);

                new Thread(handler).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}