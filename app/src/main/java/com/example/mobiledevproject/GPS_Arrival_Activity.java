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
    String destination;

    HashMap<String,Double> coordinates = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_arrival_menu);
        findViews();
        setCoordinates();
        setListeners();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destination = extras.getString("key0");
            destination_text.setText(destination);
        }
    }

    void setCoordinates() {
        coordinates.put("Afeka_lat", 32.113453);
        coordinates.put("Afeka_lon", 34.8175554);
    }

    void setListeners()
    {
        Waze_IMG.setOnClickListener((v) -> searchWaze());

        Google_Maps_IMG.setOnClickListener((v) -> searchGoogleMaps());

        Moovit_IMG.setOnClickListener((v) -> searchMoovit());
    }

    void searchWaze()
    {
        try {
            String url = "https://waze.com/ul?ll=" + coordinates.get("Afeka_lat") + "," + coordinates.get("Afeka_lon") +"&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }
    private void searchMoovit() {
        String url = "moovit://directions?dest_lat=" + coordinates.get("Afeka_lat") + "&dest_lon=" + coordinates.get("Afeka_lon") + "&dest_name=" + destination;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void searchGoogleMaps() {
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + coordinates.get("Afeka_lat") + "," + coordinates.get("Afeka_lon");
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
