package com.example.aris.navdrawersimpleweather;


import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by al-kahyatamar on 24.03.18.
 */

public class Common {
    public static String API_KEY = "cc263d54d0fcb04ab6b4aa3c1939f715";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String lat, String lon) {
        StringBuilder str = new StringBuilder(BASE_URL);
        str.append(String.format("?lat=%s&lon=%s&appid=%s", lat, lon, API_KEY));
        return str.toString();
    }

    public static String apiRequest(String city) {
        StringBuilder str = new StringBuilder(BASE_URL);
        str.append(String.format("?q=%s&appid=%s", city, API_KEY));
        return str.toString();
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp)
    {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Date date= new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    public static String getImage(String icon){
        return String.format("http://.openweathermap.org/img/w/%s.png",icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date date= new Date();
        return dateFormat.format(date);
    }
}
