package com.example.mobiledevproject;

import android.content.Context;
import android.os.Bundle;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButtonToggleGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    enum modes {
        NAVIGATION,
        LOOKAROUND
    }
//    private ActivityMainBinding binding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findViews();
        setListeners();
        setPromptState(false);

        //List<String> locations = new ArrayList<>();
        createLocationList(locations);
        //TODO: change this to upload to Firebase to then get it in other places
        List<String> temp = new ArrayList<>(locations);
        AutoSuggestAdapter adapter = new AutoSuggestAdapter(this, android.R.layout.simple_list_item_1, temp);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);
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
        reachPlaceBTN = findViewById(R.id.reach_place);
        atPlaceBTN = findViewById(R.id.im_at_place);
        autoCompleteTextView = findViewById(R.id.TXT_ACV_Search);
        textInputLayout = findViewById(R.id.textInputLayout);
        textView3 = findViewById(R.id.textView3);
    }


    //TODO: remove this when FireBase is ready
    public static List<String> getLocations() {
        return locations;
    }
}