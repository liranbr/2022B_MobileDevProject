package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mobiledevproject.Objects.Location;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    ImageView IndoorView;
    ImageView floorPlan;

    ImageButton backBtn;
    ImageButton leftBtn;
    ImageButton upBtn;
    ImageButton rightBtn;

    FloatingActionButton reportBTN;
    MaterialButtonToggleGroup toggleGroup;

    String fromWhere = "";
    String destination = "";

    Location loc = MainActivity.getLocation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromWhere = extras.getString("key0");
            destination = extras.getString("key1");
        }




        FireBaseManager.downloadImages("floor-plans/", (image) -> {
            String name = image.split(loc.getLocationName() + "-floor-")[1].split("\\.")[0];

            int size = Integer.parseInt(name);
            MaterialButton mb = new MaterialButton(this);
            mb.setText(name);
            mb.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));

            if(size > toggleGroup.getChildCount()) {
                for (int i = toggleGroup.getChildCount(); i < size; i++) {
                    toggleGroup.addView(new MaterialButton(this), i);
                }
            }
            toggleGroup.removeViewAt(size - 1);
            toggleGroup.addView(mb, size - 1);

            toggleGroup.getChildAt(size - 1).setOnClickListener(v -> {
                Glide.with(this).load(image).into(floorPlan);
            });
        });

        setListeners();
    }

    void setListeners() {


    }

    void findViews() {
        IndoorView = findViewById(R.id.Navigation_IMG_Indoor);
        floorPlan = findViewById(R.id.Navigation_IMG_Floor);
        backBtn = findViewById(R.id.Navigation_IMGBTN_back);
        leftBtn = findViewById(R.id.Navigation_IMGBTN_left);
        upBtn = findViewById(R.id.Navigation_IMGBTN_up);
        rightBtn = findViewById(R.id.Navigation_IMGBTN_right);
        reportBTN = findViewById(R.id.Navigation_FAB_report);
        toggleGroup = findViewById(R.id.Navigation_MBTG_navigation);
    }
}