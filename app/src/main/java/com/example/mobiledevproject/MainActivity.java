package com.example.mobiledevproject;

import android.content.Context;
import android.os.Bundle;

import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Objects.Waypoint;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButtonToggleGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {


    final FirebaseAuth auth = FirebaseAuth.getInstance();

    enum modes {
        NAVIGATION,
        LOOKAROUND
    }
    TextView loginTXT;
    TextView MenuText;
    TextView textView3;
    TextInputLayout textInputLayout;
    Button reachPlaceBTN;
    Button atPlaceBTN;
    MaterialButtonToggleGroup toggleGroup;
    AutoCompleteTextView autoCompleteTextView;
    int currentMode = modes.NAVIGATION.ordinal(); // default mode is navigation
    String destination = "";

    public static List<String> locations = new ArrayList<>();
    public static List<Waypoint> waypoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth.signOut(); //just for presentation purposes, to clear the user's data

        findViews();
        setListeners();
        setPromptState(false);
        FireBaseManager.getLocation("Afeka", autoCompleteTextView, locations, waypoints);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(auth.getCurrentUser() != null) {
            loginTXT.setText(String.format("Welcome %s", Objects.requireNonNull(auth.getCurrentUser().getEmail()).split("@")[0]));
            loginTXT.setTextColor(ContextCompat.getColor(this, R.color.dark));
            loginTXT.setOnClickListener(null);
        }
    }

    void setPromptState(boolean enabled) {
        reachPlaceBTN.setEnabled(enabled);
        atPlaceBTN.setEnabled(enabled);

        int visibility = enabled ? View.VISIBLE : View.INVISIBLE;
        reachPlaceBTN.setVisibility(visibility);
        atPlaceBTN.setVisibility(visibility);

        if(enabled)
        {
            String question = "Are you currently at " + autoCompleteTextView.getText().toString().split(",")[0] + "?";
            textView3.setText(question);
        }
        else
            textView3.setText("");
    }

    void setListeners()
    {
        reachPlaceBTN.setOnClickListener((v) -> UtilityMethods.switchActivityWithData(MainActivity.this, GPSArrivalActivity.class, destination));

        atPlaceBTN.setOnClickListener((v) -> UtilityMethods.switchActivityWithData(MainActivity.this, WhereFromActivity.class, destination));

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
            UtilityMethods.closeKeyboard(MainActivity.this, autoCompleteTextView);
            String selectedItem = (String) parent.getItemAtPosition(position);
            autoCompleteTextView.setText(selectedItem);
            destination = selectedItem;
            if(currentMode == modes.NAVIGATION.ordinal()) {
                setPromptState(true);
            }
            else if(currentMode == modes.LOOKAROUND.ordinal())
            {
                //TODO: check if need to send data here
                UtilityMethods.switchActivity(MainActivity.this, NavigationActivity.class);
            }
        });

        textInputLayout.setEndIconOnClickListener(v -> {
            autoCompleteTextView.setText("");
            setPromptState(false);
        });

        loginTXT.setOnClickListener((v) -> UtilityMethods.switchActivity(MainActivity.this, LoginActivity.class));

    }

    void createLocationList(List<String> locations)
    {
        Location afeka = new Location();


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
        reachPlaceBTN = findViewById(R.id.reach_place);
        atPlaceBTN = findViewById(R.id.im_at_place);
        autoCompleteTextView = findViewById(R.id.TXT_ACV_Search);
        textInputLayout = findViewById(R.id.textInputLayout);
        textView3 = findViewById(R.id.textView3);
        loginTXT = findViewById(R.id.Main_TXT_Login);
    }

    public static List<String> getLocations() {
        return locations;
    }

}