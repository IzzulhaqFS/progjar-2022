package theozu.browser;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String textUrl = scanner.next();

        try {
            LatihanJRequest request = new LatihanJRequest(textUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}