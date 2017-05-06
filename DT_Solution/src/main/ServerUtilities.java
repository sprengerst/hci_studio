package main;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Created by Stefan Sprenger on 05.09.2016.
 *
 */
public class ServerUtilities {

    public static  String serverRoot="http://127.0.0.1:8087/dtSolution";




    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    public static String post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        String body = buildBody(params);
        System.out.println( "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            conn.disconnect();
            in.close();
            System.out.println(response.toString());
            return response.toString();

        } catch(Exception e){
            System.out.println(e.toString());
            throw e;
        }
    }

    public static JSONObject sendPostWithResponse(String serverUrl, Map<String,String> params) throws Exception{
        String result = ServerUtilities.post(serverUrl, params);
        while (result.equals("")) {
            try {
                result = ServerUtilities.post(serverUrl, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject(result);
    }



    public static String get(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        String body = buildBody(params);
        System.out.println( "GET '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("GET failed with error code " + status);
            }
            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            conn.disconnect();
            in.close();
            System.out.println(response.toString());
            return response.toString();

        } catch(Exception e){
            System.out.println(e.toString());
            throw e;
        }
    }

    public static String buildBody(Map<String, String> params){
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        return bodyBuilder.toString();
    }
}
