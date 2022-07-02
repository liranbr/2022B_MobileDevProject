package com.example.mobiledevproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Utility.UtilityMethods;

import java.util.HashMap;

public class GPSArrivalActivity extends AppCompatActivity {

    TextView place_text;
    Button next_button;
    ImageView Waze_IMG;
    ImageView Google_Maps_IMG;
    ImageView Moovit_IMG;
    String fullDestination = "";
    String destination = "";

    Location loc = MainActivity.getLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_arrival_menu);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullDestination = extras.getString("key0");
            destination = fullDestination.split(",")[0];
            place_text.setText(destination);
        }
        setListeners();
    }


    void setListeners()
    {
        next_button.setOnClickListener((v) -> {
            UtilityMethods.switchActivityWithData(
                    GPSArrivalActivity.this,
                    NavigationActivity.class, "Entrance",
                    fullDestination.split(", ")[1]);
            finish();
        });

        String latLng = loc.getEntrance_LatLng();

        double lat = Double.parseDouble(latLng.split(",")[0]);
        double lon = Double.parseDouble(latLng.split(",")[1]);

        Waze_IMG.setOnClickListener((v) -> searchWaze(destination, lat, lon));

        Google_Maps_IMG.setOnClickListener((v) -> searchGoogleMaps(destination, lat, lon));

        Moovit_IMG.setOnClickListener((v) -> searchMoovit(destination, lat, lon));
    }

    // Deep link to Waze
    void searchWaze(String dst, double lat, double lon) {
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

    // Deep link to Moovit
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

    // Deep link to Google Maps
    private void searchGoogleMaps(String dst, double lat, double lon) {
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void findViews()
    {
        place_text = findViewById(R.id.Arrival_BOX_place);
        next_button = findViewById(R.id.Arrival_BTN_next);
        Waze_IMG = findViewById(R.id.Arrival_IMG_waze);
        Google_Maps_IMG = findViewById(R.id.Arrival_IMG_google_maps);
        Moovit_IMG = findViewById(R.id.Arrival_IMG_moovit);

    }



}
