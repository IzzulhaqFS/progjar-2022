package theozu.browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LatihanJRequest {

    private final String USERNAME = "theozu";
    private final String PASS = "12345678";

    public LatihanJRequest(String textUrl) throws IOException {
        setConnection(textUrl);
    }

    private void setConnection(String textUrl) throws IOException {
        // Connect to server with URL
        URL url = new URL(textUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Performing basic authentication request
        String auth = USERNAME + ":" + PASS;
        byte[] encoded = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + new String(encoded));

        int status = connection.getResponseCode();
        System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());

        // Check redirections
        if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = connection.getHeaderField("Location");
            URL newUrl = new URL(location);
            connection = (HttpURLConnection) newUrl.openConnection();
        }

        // Read the response
//        BufferedReader streamReader = null;
//
//        if (status > 299) {
//            streamReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
//        }
//        else {
//            streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        }

        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = streamReader.readLine()) != null) {
                content.append(inputLine);
                System.out.println(inputLine);
            }
            streamReader.close();
        }

//        System.out.println(content);

        // Close connection
        connection.disconnect();
    }
}
