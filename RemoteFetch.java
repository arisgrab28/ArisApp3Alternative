package com.example.aris.navdrawersimpleweather;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aris on 18.03.2018.
 */

public class RemoteFetch {
   // "http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=cc263d54d0fcb04ab6b4aa3c1939f715"

    public static JSONObject getJSON(Context context, String httpurl){

        try {

            URL url = new URL(httpurl);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(10000);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}
