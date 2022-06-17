package com.example.mobiledevproject;

import android.os.Bundle;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobiledevproject.ui.main.SectionsPagerAdapter;
//import com.example.mobiledevproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    enum modes {
        NAVIGATION,
        LOOKAROUND
    }
//    private ActivityMainBinding binding;
    TextView MenuText;
    Button reachLocationBTN;
    Button onSiteBTN;
    MaterialButtonToggleGroup toggleGroup;
    int currentMode = modes.NAVIGATION.ordinal(); // default mode is navigation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findviews();
        reachLocationBTN.setEnabled(false);
        onSiteBTN.setEnabled(false);

        reachLocationBTN.setVisibility(View.INVISIBLE);
        onSiteBTN.setVisibility(View.INVISIBLE);

        toggleGroup.addOnButtonCheckedListener((MaterialButtonToggleGroup group, int checkedId, boolean isChecked)-> {
                if (isChecked) {
                    if(checkedId == R.id.button1) {
                        currentMode = modes.NAVIGATION.ordinal();
                        MenuText.setText("Where To?");
                    } else if(checkedId == R.id.button2) {
                        currentMode = modes.LOOKAROUND.ordinal();
                        MenuText.setText("Look Where?");
                    }
                }
        });

        /* // Instantiate tabs and viewpager
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        */
    }

    void findviews()
    {
        MenuText = findViewById(R.id.textView2);
        toggleGroup = findViewById(R.id.toggleButton);
        reachLocationBTN = findViewById(R.id.reach_location);
        onSiteBTN = findViewById(R.id.im_at_location);
    }
}