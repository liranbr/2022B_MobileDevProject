package com.example.mobiledevproject;

import android.content.Context;
import android.os.Bundle;

import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobiledevproject.ui.main.SectionsPagerAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
//import com.example.mobiledevproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    enum modes {
        NAVIGATION,
        LOOKAROUND
    }
//    private ActivityMainBinding binding;
    TextView MenuText;
    TextView textView3;
    TextInputLayout textInputLayout;
    Button reachLocationBTN;
    Button onSiteBTN;
    MaterialButtonToggleGroup toggleGroup;
    AutoCompleteTextView autoCompleteTextView;
    int currentMode = modes.NAVIGATION.ordinal(); // default mode is navigation
    String destination = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findViews();
        setListeners();
        setPromptState(false);

        List<String> locations = new ArrayList<>();
        createLocationList(locations);

        AutoSuggestAdapter adapter = new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, locations);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);


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

    void setPromptState(boolean enabled) {
        reachLocationBTN.setEnabled(enabled);
        onSiteBTN.setEnabled(enabled);

        int visibility = enabled ? View.VISIBLE : View.INVISIBLE;
        reachLocationBTN.setVisibility(visibility);
        onSiteBTN.setVisibility(visibility);

        if(enabled)
        {
            String question = "Are you at " + autoCompleteTextView.getText().toString().split(",")[0] + "?";
            textView3.setText(question);
        }
        else
            textView3.setText("");
    }

    void setListeners()
    {
        reachLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityMethods.switchActivityWithData(MainActivity.this, GPS_Arrival_Activity.class, destination.split(",")[0]);
            }
        });

        toggleGroup.addOnButtonCheckedListener((MaterialButtonToggleGroup group, int checkedId, boolean isChecked)-> {
            if (isChecked) {
                autoCompleteTextView.setText("");
                if(checkedId == R.id.button1) {
                    currentMode = modes.NAVIGATION.ordinal();
                    MenuText.setText("Where To?");
                } else if(checkedId == R.id.button2) {
                    currentMode = modes.LOOKAROUND.ordinal();
                    MenuText.setText("Look Where?");
                    setPromptState(false);
                }
            }
        });


        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            closeKeyboard();
            String selectedItem = (String) parent.getItemAtPosition(position);
            autoCompleteTextView.setText(selectedItem);
            destination = selectedItem;
            if(currentMode == modes.NAVIGATION.ordinal()) {
                setPromptState(true);
            }
            else if(currentMode == modes.LOOKAROUND.ordinal())
            {
                //TODO: switch to lookaround view
            }
        });

        textInputLayout.setEndIconOnClickListener(v -> {
            autoCompleteTextView.setText("");
            setPromptState(false);
        });

    }

    void createLocationList(List<String> locations)
    {
        for (int i = 0; i <= 9; i++) {
            locations.add("Afeka College, class " + (i + 100));
            locations.add("Afeka College, class " + (i + 200));
        }

        for(int i = 0; i <= 4; i++) {
            locations.add("Afeka College, class " + (i + 300));
        }

    }


    void findViews()
    {
        MenuText = findViewById(R.id.textView2);
        toggleGroup = findViewById(R.id.toggleButton);
        reachLocationBTN = findViewById(R.id.reach_location);
        onSiteBTN = findViewById(R.id.im_at_location);
        autoCompleteTextView = findViewById(R.id.TXT_ACV_Search);
        textInputLayout = findViewById(R.id.textInputLayout);
        textView3 = findViewById(R.id.textView3);
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}