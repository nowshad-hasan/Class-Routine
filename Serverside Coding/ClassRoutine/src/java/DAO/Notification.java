/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.google.gson.JsonObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author nowshad
 */
public class Notification {
     
    public final static String AUTH_KEY_FCM = "";   // AUTH_FCM_KEY will be here
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    
    public void sendNotification(String message,String topicSession)
    {
                    String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;
        String userDeviceIdKey = "/topics/"+topicSession;

        try {

            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JsonObject json = new JsonObject();
            json.addProperty("to", userDeviceIdKey.trim());
            JsonObject info = new JsonObject();
            info.addProperty("title", "Daily Routine "); // Notification title
            info.addProperty("body", message); // Notification body
            json.add("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    

    
}
