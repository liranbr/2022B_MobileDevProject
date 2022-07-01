package com.example.mobiledevproject;

import android.os.Bundle;

import com.example.mobiledevproject.Objects.Graph;
import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Objects.Waypoint;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButtonToggleGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    int currentMode = modes.LOOKAROUND.ordinal(); // default mode is navigation
    String destination = "";


    public static Location loc = new Location();
    public static Graph<String> graph = new Graph<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth.signOut(); //just for presentation purposes, to clear the user's data

        findViews();
        setListeners();
        setPromptState(false);
        FireBaseManager.getLocation("Afeka", autoCompleteTextView, loc, graph);
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
                UtilityMethods.switchActivityWithData(MainActivity.this, NavigationActivity.class, destination.split(", ")[1]);
            }
        });

        textInputLayout.setEndIconOnClickListener(v -> {
            autoCompleteTextView.setText("");
            setPromptState(false);
        });

        loginTXT.setOnClickListener((v) -> UtilityMethods.switchActivity(MainActivity.this, LoginActivity.class));

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


    public static List<String> getPoiNames() {

        List<String> retList = new ArrayList<>();
        for(String poi : loc.getPOIs().keySet()) {
            retList.add(loc.getLocationName() + ", " + poi);
        }

        return retList;
    }

    public static Location getLocation() {
        return loc;
    }

    public static Graph<String> getGraph() { return graph; }

}