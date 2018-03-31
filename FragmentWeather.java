package com.example.aris.navdrawersimpleweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aris.navdrawersimpleweather.Model.Openweather;
import com.example.aris.navdrawersimpleweather.Remote.WeatherAPI;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentWeather.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentWeather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWeather extends Fragment {
    public static String API_KEY = "cc263d54d0fcb04ab6b4aa3c1939f715";
    Typeface weatherFont;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView timeField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    private static Location currentlocation;
    private LocationManager locationmanager;
     int MY_PERMISSION=0;
    //Openweather openWeatherMap= new Openweather();
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public FragmentWeather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment FragmentWeather.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWeather newInstance(String param1) {
        FragmentWeather fragment = new FragmentWeather();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "font/weather.ttf");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView) view.findViewById(R.id.city_field);
        updatedField = (TextView) view.findViewById(R.id.updated_field);
        detailsField = (TextView) view.findViewById(R.id.details_field);
        timeField = (TextView) view.findViewById(R.id.time_field);
        currentTemperatureField = (TextView) view.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) view.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locationmanager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]
            {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION);

        }
        currentlocation = locationmanager.getLastKnownLocation(bestprovider);
        float lon;
        float lat;
        if (currentlocation != null) {
             lon = (float)16.36;//(float)currentlocation.getLongitude();
             lat = (float)48.21;//(float)currentlocation.getLatitude();
        } else {
            GPSTracker d = new GPSTracker(getActivity());
             lon = (float)16.36;//d.getLongitude();
             lat = (float)48.21;// d.getLatitude();
        }

        WeatherAPI.Factory.getInstance().getWeather(lat,lon,API_KEY).enqueue(new Callback<Openweather>() {
            @Override
            public void onResponse(Call<Openweather> call, Response<Openweather> response) {

                cityField.setText(String.format("%s, %s",response.body().getName() ,response.body().getSys().getCountry()));
                updatedField.setText(String.format("last update: %s", response.body().getDateNow()));
                detailsField.setText(String.format("%s\nHumidity: %s\nPressure: %s\n",
                        response.body().getWeather().get(0).getDescription(),
                        response.body().getMain().getHumidity(),
                        response.body().getMain().getPressure()));
                timeField.setText(String.format("%s/%s", Common.unixTimeStampToDateTime( response.body().getSys().getSunrise()),
                        Common.unixTimeStampToDateTime( response.body().getSys().getSunset()) ));
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
        //new updateWeather().execute(Common.apiRequest(String.format("%.0f",lat),String.format("%.0f",lon)));
        //updateWeatherData(WeatherUrl);
        onButtonPressed();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed( ) {
        if (mListener != null) {
            mListener.onFragmentInteraction("OK");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String str);
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
//
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
//            timeField.setText(String.format("%s/%s", Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset()) ));
//            currentTemperatureField.setText(String.format("%.2f ℃", openWeatherMap.getMain().getTemp()-273.5));
//            setWeatherIcon(openWeatherMap.getWeather().get(0).getId(),
//                    (long)openWeatherMap.getSys().getSunrise(),
//                    (long)openWeatherMap.getSys().getSunset());
//
//            // Picasso.with (getActivity()).load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon())).into(weatherIcon);
//        }
//    }

}

