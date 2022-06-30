package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WhereFromActivity extends AppCompatActivity {

    Button whereFromBTN;
    TextView whereFromPlaceQuestion;
    TextView whereFromDestination;
    ImageView floorPlan;
    AutoCompleteTextView whereFromACTV;

    String location = "";
    String place = "";
    String destination = "";

    List<String> locations = new ArrayList<>();
    List<String> imageURLs = new ArrayList<>();

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
            String fromWhereQuestion = "From where in " + place + "?";
            whereFromPlaceQuestion.setText(fromWhereQuestion);
            whereFromDestination.setText(destination);
        }

        locations = getRelevantLocations(place);

        FireBaseManager.downloadImages("floor-plans/", (image) -> {
            imageURLs.add(image);
        });

        AutoSuggestAdapter adapter = new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, locations);
        whereFromACTV.setAdapter(adapter);
        whereFromACTV.setThreshold(3);

        setListeners();
    }

    void setListeners() {
        whereFromACTV.setOnItemClickListener((parent, view, position, id) -> {
            UtilityMethods.closeKeyboard(WhereFromActivity.this, whereFromACTV);
            String selected = (String) parent.getItemAtPosition(position);
            whereFromACTV.setText(selected);
            Collections.sort(imageURLs);

            int index;
            if(selected.contains("1F") || selected.contains("20") || selected.contains("Reception"))
                index = 0;
            else
                index = 1;

            Glide.with(this).load(imageURLs.get(index)).placeholder(R.drawable.compus_logo).into(floorPlan);

        });

        whereFromBTN.setOnClickListener((v) -> {
            String fromWhere = whereFromACTV.getText().toString();
            if(!fromWhere.isEmpty())
                UtilityMethods.switchActivityWithData(
                        WhereFromActivity.this,
                        NavigationActivity.class,
                        fromWhere.split(",")[1],
                        destination);
            else
                whereFromACTV.setError("Please select a location");
        });
    }

    List<String> getRelevantLocations(String place) {
        List<String> lst = new ArrayList<>();
        for(String s : MainActivity.getPoiNames()) {
            if(s.equals(location))
                continue;
            if(s.contains(place)) {
                lst.add(s);
            }
        }

        return lst;
    }

    void findViews() {
        whereFromPlaceQuestion = findViewById(R.id.WhereFrom_TXT_current_location);
        whereFromDestination = findViewById(R.id.WhereFrom_BOX_destination);
        whereFromACTV = findViewById(R.id.WhereFrom_ACTV_current_location);
        floorPlan = findViewById(R.id.WhereFrom_IMG_map);
        whereFromBTN = findViewById(R.id.WhereFrom_BTN_next);
    }
}
