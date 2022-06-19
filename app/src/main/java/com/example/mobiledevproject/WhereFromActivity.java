package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WhereFromActivity extends AppCompatActivity {

    TextView whereFromPlace;
    TextView whereFromDestination;
    AutoCompleteTextView whereFromACTV;

    String location = "";
    String place = "";
    String destination = "";

    List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.where_from_menu);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location = extras.getString("key0");
            place = location.split(",")[0];
            destination = location.split(",")[1];

            whereFromPlace.setText(place);
            whereFromDestination.setText(destination);
        }

        locations = getRelevantLocations(place);
        AutoSuggestAdapter adapter = new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, locations);
        whereFromACTV.setAdapter(adapter);
        whereFromACTV.setThreshold(3);

        setListeners();
    }

    void setListeners() {
        whereFromACTV.setOnItemClickListener((parent, view, position, id) -> {
            UtilityMethods.closeKeyboard(WhereFromActivity.this, whereFromACTV);
            String selected = (String) parent.getItemAtPosition(position);
            whereFromPlace.setText(selected);
        });
    }

    //TODO: when FireBase is ready get the list of original locations from it
    List<String> getRelevantLocations(String place) {
        List<String> lst = new ArrayList<>();
        for(String s : MainActivity.getLocations()) {
            if(s.equals(location))
                continue;
            if(s.contains(place)) {
                lst.add(s);
            }
        }

        return lst;
    }

    void findViews() {
        whereFromPlace = findViewById(R.id.WhereFrom_BOX_place);
        whereFromDestination = findViewById(R.id.WhereFrom_BOX_destination);
        whereFromACTV = findViewById(R.id.WhereFrom_ACTV_current_location);
    }
}