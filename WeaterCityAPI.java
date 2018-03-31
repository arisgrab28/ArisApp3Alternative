package com.example.aris.navdrawersimpleweather.Remote;

import com.example.aris.navdrawersimpleweather.Model.Openweather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeaterCityAPI {
    String BASE_URL="http://api.openweathermap.org";
    @GET("/data/2.5/weather")
    Call<Openweather> getCityWeather(@Query("q") String city,  @Query("appid") String appID);


    class Factory {
        public static WeaterCityAPI service;

        public static WeaterCityAPI getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(WeaterCityAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
