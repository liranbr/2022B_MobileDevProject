package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavigationActivity extends AppCompatActivity {

    ImageView IndoorView;
    ImageView floorPlan;

    ImageButton backBtn;
    ImageButton leftBtn;
    ImageButton upBtn;
    ImageButton rightBtn;

    FloatingActionButton reportBTN;
    MaterialButtonToggleGroup toggleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        findViews();


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