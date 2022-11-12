package id.ac.its.izzulhaq.chatclient.thread;

import id.ac.its.izzulhaq.chatclient.ChatClient;
import id.ac.its.izzulhaq.chatclient.des.DES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader br;
    private final ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
        }
        catch (IOException e) {
            System.out.println("Error occurred on ReadThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = br.readLine();
                DES decipher = new DES();

                String KEY = "7510F92FC6E8E3DA";

//                String[] splitResponse = response.split(" ");
                String userName = response.substring(0, response.indexOf(":") - 1);
                String message = response.substring(response.indexOf(":") + 2);

                if (!userName.equals("Server")) {
                    int n = message.length();
                    if (n != 16) {
                        StringBuilder sb = new StringBuilder();
                        String temp;

                        for (int i = 0; i < n; i += 16) {
                            temp = message.substring(i, i + 16);
                            sb.append(decipher.decrypt(temp, KEY));
                        }

                        message = sb.toString();
                    }
                    else {
                        message = decipher.decrypt(message, KEY);
                    }
                    message = hexToString(message);
                }

                response = userName + " : " + message;

                System.out.println("\n" + response);

                if (client.getUserName() != null) {
                    System.out.print(client.getUserName() + " : ");
                }
            }
            catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private String hexToString(String hexStr) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            sb.append((char) Integer.parseInt(str, 16));
        }

        return sb.toString();
    }
}
