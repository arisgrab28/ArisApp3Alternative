package com.example.aris.navdrawersimpleweather;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.aris.navdrawersimpleweather.Model.Openweather;
import com.example.aris.navdrawersimpleweather.Remote.WeaterCityAPI;
import com.example.aris.navdrawersimpleweather.Remote.WeatherAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {
    private String cityUrl="http://app.openweathermap.org/data/2.5/weather?q=Vienna,AUT&appid=b6907d289e10d714a6e88b30761fae22";
    private Spinner sp1;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    public static String API_KEY = "cc263d54d0fcb04ab6b4aa3c1939f715";
    Typeface weatherFont;
    public CityFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View cityview= inflater.inflate(R.layout.fragment_city, container, false);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "font/weather.ttf");
        cityField = (TextView) cityview.findViewById(R.id.city_field);
        updatedField = (TextView) cityview.findViewById(R.id.updated_field);
        detailsField = (TextView) cityview.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) cityview.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) cityview.findViewById(R.id.weather_icon);
        sp1=(Spinner)cityview.findViewById(R.id.spinner);
        weatherIcon.setTypeface(weatherFont);
        //handler = new Handler();
        final List<String> Array=new ArrayList<String>();
        Array.add("Vienna");
        Array.add("Graz");
        Array.add("Salzburg");
        Array.add("Eisenstadt");
        Array.add("Innsbruck");
        Array.add("Bregenz");
        Array.add("Sankt Poelten");
        Array.add("Klagenfurt");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, Array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        cityUrl="Vienna,AUT";
                        break;
                    case 1:
                        cityUrl="Graz,AUT";
                        break;
                    case 2:
                        cityUrl="Salzburg,AUT";
                        break;
                    case 3:
                        cityUrl="Eisenstadt,AUT";
                        break;
                    case 4:
                        cityUrl="Innsbruck,AUT";
                        break;
                    case 5:
                        cityUrl="Bregenz,AUT";
                        break;
                    case 6:
                        cityUrl="St. Pölten,AUT";
                        break;
                    case 7:
                        cityUrl="Klagenfurt,AUT";
                        break;
                }
                WeaterCityAPI.Factory.getInstance().getCityWeather(cityUrl,API_KEY).enqueue(new Callback<Openweather>() {
                    @Override
                    public void onResponse(Call<Openweather> call, Response<Openweather> response) {

                        cityField.setText(String.format("%s, %s",response.body().getName() ,response.body().getSys().getCountry()));
                        updatedField.setText(String.format("last update: %s", response.body().getDateNow()));
                        detailsField.setText(String.format("%s\nHumidity: %s\nPressure: %s\n",
                                response.body().getWeather().get(0).getDescription(),
                                response.body().getMain().getHumidity(),
                                response.body().getMain().getPressure()));
                        currentTemperatureField.setText(String.format("%.2f ℃",  response.body().getMain().getTemp()-273.5));
                        setWeatherIcon( response.body().getWeather().get(0).getId(),
                                (long) response.body().getSys().getSunrise(),
                                (long) response.body().getSys().getSunset());
                    }

                    @Override
                    public void onFailure(Call<Openweather> call, Throwable t) {
                        Log.e("Faild",t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        return cityview;
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }


//    private class updateWeather extends AsyncTask<String, Void,String> {
//        ProgressDialog pd = new ProgressDialog(getActivity());
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setTitle("please wait...");
//            pd.show();
//
//        }

//        @Override
//        protected String doInBackground(String... params) {
//            String stream = null;
//            String urlString = params[0];
//            Helper http = new Helper();
//            stream = http.getHTTPData(urlString);
//            return stream;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if(s.contains("Error: not found ")){
//                pd.dismiss();
//                return;
//            }
//            Gson gson = new Gson();
//            Type mytype= new TypeToken<OpenWeatherMap>(){}.getType();
//            openWeatherMap=gson.fromJson(s,mytype);
//            pd.dismiss();
//            cityField.setText(String.format("%s, %s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry()));
//            updatedField.setText(String.format("last update: %s", Common.getDateNow()));
//            detailsField.setText(String.format("%s\nHumidity: %s\nPressure: %s\n", openWeatherMap.getWeather().get(0).getDescription(),openWeatherMap.getMain().getHumidity(),
//                    openWeatherMap.getMain().getPressure()));
//          //  timeField.setText(String.format("%s/%s", Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset()) ));
//            currentTemperatureField.setText(String.format("%.2f ℃", openWeatherMap.getMain().getTemp()-273.5));
//            setWeatherIcon(openWeatherMap.getWeather().get(0).getId(),
//                    (long)openWeatherMap.getSys().getSunrise(),
//                    (long)openWeatherMap.getSys().getSunset());
//
//        }
//    }
}
