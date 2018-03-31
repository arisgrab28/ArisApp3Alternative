package com.example.aris.navdrawersimpleweather;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {

    private WebView Wetter;
    private static Location currentlocation;
    private LocationManager locationmanager;
    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View webview = inflater.inflate(R.layout.fragment_web, container, false);
        Wetter = (WebView) webview.findViewById(R.id.WetterWeb);
        Wetter.getSettings().setBuiltInZoomControls(true);
        Wetter.getSettings().setLoadWithOverviewMode(true);
        Wetter.getSettings().setUseWideViewPort(true);

        locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestprovider = locationmanager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        currentlocation = locationmanager.getLastKnownLocation(bestprovider);

        if (currentlocation != null) {
            String cityName = null;
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (cityName.equals("Wien")) {
                Wetter.loadUrl("http://wetter.orf.at/wien/");
            } else if (cityName.equals("Steiermark")) {
                Wetter.loadUrl("http://wetter.orf.at/steiermark/");
            } else if (cityName.equals("Salzburg")) {
                Wetter.loadUrl("http://wetter.orf.at/salzburg");
            } else if (cityName.equals("Burgenland")) {
                Wetter.loadUrl("http://wetter.orf.at/burgenland/");
            } else if (cityName.equals("Tirol")) {
                Wetter.loadUrl("http://wetter.orf.at/tirol/");
            } else if (cityName.equals("Vorarlberg")) {
                Wetter.loadUrl("http://wetter.orf.at/vorarlberg/");
            } else if (cityName.equals("Niederösterreich")) {
                Wetter.loadUrl("http://wetter.orf.at/niederoesterreich/");
            } else if (cityName.equals("Kärnten")) {
                Wetter.loadUrl("http://wetter.orf.at/kaernten");
            }


        }

        return webview;


    }
  }
