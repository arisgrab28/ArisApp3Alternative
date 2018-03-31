package com.example.aris.navdrawersimpleweather.Remote;

import com.example.aris.navdrawersimpleweather.Model.Openweather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by al-kahyatamar on 31.03.18.
 */

public interface WeatherAPI {
    String BASE_URL="http://api.openweathermap.org";
    @GET("/data/2.5/weather")
    Call<Openweather> getWeather(@Query("lat") float lat, @Query("lon") float lan,  @Query("appid") String appID);

    class Factory {
        public static WeatherAPI service;

        public static WeatherAPI getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(WeatherAPI.class);
                return service;
            }
            else {
                return service;
            }
        }

    }

}
