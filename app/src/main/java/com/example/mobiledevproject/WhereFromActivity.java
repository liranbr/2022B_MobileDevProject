package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WhereFromActivity extends AppCompatActivity {

    Button startNavBtn;
    TextView whereFromPlaceQuestion;
    TextView whereFromDestination;
    ImageView floorPlan;
    AutoCompleteTextView whereFromACTV;
    Location location;

    String locationName = "";
    String destination = "";

    List<String> destinations = new ArrayList<>();
    HashMap<String, String> imageURLs = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.where_from_menu);
        findViews();
        Bundle extras = getIntent().getExtras();
        location = MainActivity.getLocation();
        if (extras != null) {
            destination = extras.getString("key0").split(", ")[1];
            locationName = location.getLocationName();
            String fromWhereQuestion = "From where in " + locationName + "?";
            whereFromPlaceQuestion.setText(fromWhereQuestion);
            whereFromDestination.setText(destination);
        }

        destinations = getRelevantPOIs(locationName);

        FireBaseManager.downloadImages("floor-plans/", (image) -> {
            String floorName = image.split(locationName + "-floor-")[1].split("\\.")[0];
            imageURLs.put(floorName, image);
        });

        AutoSuggestAdapter adapter = new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, destinations);
        whereFromACTV.setAdapter(adapter);
        whereFromACTV.setThreshold(3);

        setListeners();
    }

    void setListeners() {
        whereFromACTV.setOnItemClickListener((parent, view, position, id) -> {
            UtilityMethods.closeKeyboard(WhereFromActivity.this, whereFromACTV);
            String selectedPOI = (String) parent.getItemAtPosition(position);
            whereFromACTV.setText(selectedPOI);

            String currentFloor = location.getWaypointToFloor().get(location.getPOIs().get(selectedPOI));
            Glide.with(this).load(imageURLs.get(currentFloor)).placeholder(R.drawable.compus_logo).into(floorPlan);

        });

        startNavBtn.setOnClickListener((v) -> {
            String fromWhere = whereFromACTV.getText().toString();
            if(!fromWhere.isEmpty())
                UtilityMethods.switchActivityWithData(
                        WhereFromActivity.this,
                        NavigationActivity.class,
                        fromWhere,
                        destination);
            else
                whereFromACTV.setError("Please select a location");
        });
    }

    List<String> getRelevantPOIs(String place) {
        List<String> lst = new ArrayList<>();
        for(String s : location.getPOIs().keySet()) {
            if(s.equals(destination))
                continue;
            lst.add(s);
        }
        return lst;
    }

    void findViews() {
        whereFromPlaceQuestion = findViewById(R.id.WhereFrom_TXT_current_location);
        whereFromDestination = findViewById(R.id.WhereFrom_BOX_destination);
        whereFromACTV = findViewById(R.id.WhereFrom_ACTV_current_location);
        floorPlan = findViewById(R.id.WhereFrom_IMG_map);
        startNavBtn = findViewById(R.id.WhereFrom_BTN_startNav);
    }
}
