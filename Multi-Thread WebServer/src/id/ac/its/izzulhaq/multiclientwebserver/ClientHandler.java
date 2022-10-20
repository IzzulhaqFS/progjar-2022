package id.ac.its.izzulhaq.multiclientwebserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final String rootDir;

    public ClientHandler(Socket socket, String dir) {
        this.clientSocket = socket;
        this.rootDir = dir;
    }

    @Override
    public void run() {
        BufferedWriter out = null;
        BufferedReader in = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                String urn = line.split(" ")[1];
                System.out.println(urn);
                urn = urn.substring(1);

                String fileName = urn.substring(urn.lastIndexOf("/") + 1);

                Path path = new File(fileName).toPath();
                String contentType = Files.probeContentType(path);
                String fileContent;
                String statusCode;

                String dirPath = rootDir + urn;
                String currentDir = dirPath.substring(dirPath.lastIndexOf("/") + 1);

                if (urn.contains(".")) {
                    if (contentType.equals("text/html")) {
                        try {
                            FileInputStream fis = new FileInputStream(dirPath);
                            fileContent = new String(fis.readAllBytes());
                            statusCode = "200 OK";
                        }
                        catch (FileNotFoundException e) {
                            fileContent = "File not found";
                            statusCode = "404 Not Found";
                        }

                        out.write("HTTP/1.1 " + statusCode + "\r\n");
                        out.write("Content-Type: " + contentType + "\r\n");
                        out.write("Content-Length: " + fileContent.length() + "\r\n\r\n");
                        out.write(fileContent);
                        out.flush();
                    }
                    else {
                        try {
                            FileInputStream fis = new FileInputStream(dirPath);
                            fileContent = new String(fis.readAllBytes());
                            statusCode = "200 OK";

                            out.write("HTTP/1.1 " + statusCode + "\r\n");
                            out.write("Content-Type: " + contentType + "\r\n");
                            out.write("Content-Length: " + fileContent.length() + "\r\n\r\n");
                            out.write("Content-Disposition: attachment; filename=" + "\"" + fileName + "\"" + "\r\n\r\n");
                            out.write(fileContent);
                            out.flush();
                        }
                        catch (FileNotFoundException e) {
                            fileContent = "File not found";
                            statusCode = "404 Not Found";

                            System.out.println("File not found");
                            out.write("HTTP/1.1 " + statusCode + "\r\n\r\n");
                            out.write(fileContent);
                            out.flush();
                        }
                    }
                }
                else {
                    File dir = new File(dirPath);

                    File[] files = dir.listFiles();
                    if (files != null) {
                        fileContent = "<html>\r\n"
                                + "<body>"
                                + "<table>"
                                + "<tr>"
                                + "<th>Name</th>"
                                + "<th>Last Modified</th>"
                                + "<th>Size</th>"
                                + "</tr>";

                        for (File file : files) {
                            contentType = Files.probeContentType(new File(file.getName()).toPath());
                            String fileContent2 = file.getName();
                            Date lastModified = new Date(file.lastModified());

                            if (file.getName().equals("index.html")) {
                                FileInputStream fis = new FileInputStream(file);
                                fileContent2 = new String(fis.readAllBytes());
                                statusCode = "200 OK";

                                out.write("HTTP/1.1 " + statusCode + "\r\n");
                                out.write("Content-Type: " + contentType + "\r\n");
                                out.write("Content-Length: " + fileContent.length() + "\r\n\r\n");
                                out.write(fileContent);
                                out.flush();
                            }
                            else {
                                fileContent += "<tr>"
                                        + "<td><a href=\"" + currentDir + "/" + file.getName() + "\">" + file.getName() + "</a></td>"
                                        + "<td>" + lastModified + "</td>"
                                        + "<td>" + file.length() + " Bytes</td>"
                                        + "</tr>";
                            }
                        }
                        fileContent += "</table>"
                                + "</body>\r\n"
                                + "</html>";
                        out.write("HTTP/1.1 200 OK" + "\r\n\r\n");
                        out.write(fileContent);
                    }
                    else {
                        fileContent = "Directory not found";
                        statusCode = "404 Not Found";

                        out.write("HTTP/1.1 " + statusCode + "\r\n\r\n");
                        out.write(fileContent);
                    }
                    out.flush();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
