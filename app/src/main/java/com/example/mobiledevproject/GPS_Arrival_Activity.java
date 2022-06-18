package com.example.mobiledevproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;

public class GPS_Arrival_Activity extends AppCompatActivity {

    TextView destination_text;
    Button button;
    ImageView Waze_IMG;
    ImageView Google_Maps_IMG;
    ImageView Moovit_IMG;
    String destination = "";

    HashMap<String,Double> coordinates = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_arrival_menu);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destination = extras.getString("key0");
            destination_text.setText(destination);
        }
        setCoordinates();
        setListeners();
    }

    void setCoordinates() {
        coordinates.put("Afeka College_lat", 32.113453);
        coordinates.put("Afeka College_lon", 34.8175554);
    }

    void setListeners()
    {
        Double lat = coordinates.get(destination + "_lat");
        Double lon = coordinates.get(destination + "_lon");

        assert lat != null && lon != null;

        Waze_IMG.setOnClickListener((v) -> searchWaze(destination, lat, lon));

        Google_Maps_IMG.setOnClickListener((v) -> searchGoogleMaps(destination, lat, lon));

        Moovit_IMG.setOnClickListener((v) -> searchMoovit(destination, lat, lon));
    }

    void searchWaze(String dst, double lat, double lon)
    {
        try {
            String url = "https://waze.com/ul?ll=" + lat + "," + lon +"&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }
    private void searchMoovit(String dst, double lat, double lon) {
        String url = "moovit://directions?dest_lat=" + lat + "&dest_lon=" + lon + "&dest_name=" + dst;
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // If Moovit is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tranzmate"));
            startActivity(intent);
        }
    }

    private void searchGoogleMaps(String dst, double lat, double lon) {
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void findViews()
    {
        destination_text = findViewById(R.id.destination_text);
        button = findViewById(R.id.button);
        Waze_IMG = findViewById(R.id.Waze_IMG);
        Google_Maps_IMG = findViewById(R.id.Google_Maps_IMG);
        Moovit_IMG = findViewById(R.id.Moovit_IMG);

    }



}
