package com.example.mobiledevproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mobiledevproject.Objects.Graph;
import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Utility.FireBaseManager;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class NavigationActivity extends AppCompatActivity {

    TextToSpeech moshe;
    ImageView[] IndoorViews;
    ViewFlipper viewFlipper;

    ImageView floorPlan;

    ImageButton backBtn;
    ImageButton leftBtn;
    ImageButton forwardBtn;
    ImageButton rightBtn;

    FloatingActionButton reportBTN;
    MaterialButtonToggleGroup toggleGroup;

    boolean isNavigation = false;
    int currentDirection = 0; // 0 = up, 1 = right, 2 = down, 3 = left
    String fromWherePOI = "";
    String fromWhereId = "";
    String currentWaypoint = "";
    String destinationPOI = "";
    String destinationId = "";
    String[] directions = {"up", "right", "down", "left"};
    String[] shortestPathDirections = new String[0];
    List<String> shortestPath = new ArrayList<>();

    Stack<String> previousWaypoints = new Stack<>();

    HashMap<String, Bitmap> waypointImages = new HashMap<>();
    Location loc = new Location();
    Graph<String> waypointsGraph = MainActivity.getGraph();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loc = MainActivity.getLocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromWherePOI = extras.getString("key0");
            if (extras.containsKey("key1")) {
                destinationPOI = extras.getString("key1");
                if(!destinationPOI.isEmpty())
                    isNavigation = true;
            }
            fromWhereId = loc.getPOIs().get(fromWherePOI);
            currentWaypoint = fromWhereId;
            destinationId = loc.getPOIs().get(destinationPOI);
        }
        moshe=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    moshe.setLanguage(Locale.US);
                    if (isNavigation)
                        moshe.speak("Welcome to the navigation screen", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        // Download indoor view images for all waypoints and load the relevant one into the image view
        FireBaseManager.downloadImages("waypoint-images", (image) -> {
            String[] imageNameParts = image.split("\\.jpg")[0].split("%2F");
            String imageName = imageNameParts[imageNameParts.length - 1];
            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            waypointImages.put(imageName, resource);
                            String[] innerImageNameParts = imageName.split("-");
                            String imageDirection = innerImageNameParts[innerImageNameParts.length - 1];
                            if (imageName.contains(currentWaypoint))
                                IndoorViews[Arrays.asList(directions).indexOf(imageDirection)].setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        });

        String[] floors = loc.getFloors().split(",");
        int floorCount = floors.length;

        // Setup Toggle Group according to floor amount
        for (int i = 0; i < floorCount; i++) {
            MaterialButton mb = new MaterialButton(this);
            mb.setText(floors[i]);
            mb.setPadding(0, 0, 0, 0);
            mb.setTextColor(ContextCompat.getColorStateList(this, R.color.selectable_floor_button_text));
            mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selectable_floor_button_background));

            toggleGroup.addView(mb, i);
        }

        // Download floor plan images and set the correct one
        FireBaseManager.downloadImages("floor-plans/", (image) -> {
            String floorName = image.split(loc.getLocationName() + "-floor-")[1].split("\\.")[0];

            int index = Arrays.asList(floors).indexOf(floorName);
            toggleGroup.getChildAt(index).setOnClickListener(v -> {
                Glide.with(this).load(image).into(floorPlan);
            });

            updateFloor();
        });

        checkCanMoveForward();
        if(isNavigation){
            makeShortestPath();
            getCurrentStep();
        }
        setListeners();
    }

    //traverse the shortest path in the graph and create directions list
    private void makeShortestPath() {
        shortestPath = waypointsGraph.getShortestPath(fromWhereId, destinationId);
        shortestPathDirections = new String[shortestPath.size()];
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            shortestPathDirections[i] = waypointsGraph.getNeighborDirection(shortestPath.get(i), shortestPath.get(i + 1));
        }
    }

    // Get the current needed step in the found path and highlight the correct arrow
    private void getCurrentStep() {
        leftBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));
        if (forwardBtn.isEnabled())
            forwardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));
        rightBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));
        backBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark));
        int index = shortestPath.indexOf(currentWaypoint);
        if(index == -1) {
            backBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_500));
            moshe.speak("Turn back.", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else if (index == shortestPath.size() - 1) {
            // If Reached Destination - Color all the arrows green
            leftBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
            forwardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
            rightBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
            backBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_500));
            moshe.speak("You have reached your destination!", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            String stepDirection = shortestPathDirections[index];
            if(stepDirection.equals(directions[currentDirection])) {
                forwardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
            }
            else if(stepDirection.equals(directions[(currentDirection + 1) % 4])) {
                rightBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
                moshe.speak("Turn right.", TextToSpeech.QUEUE_FLUSH, null, null);
            }
            else {
                leftBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
                moshe.speak("Turn left.", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    // Check if moved up or down a floor and update accordingly
    void updateFloor() {
        String currentFloor = loc.getWaypointToFloor().get(currentWaypoint);
        for (int i = 0; i < toggleGroup.getChildCount(); i++) {
            MaterialButton child = (MaterialButton) toggleGroup.getChildAt(i);
            if (child.getText().toString().equals(currentFloor)) {
                toggleGroup.check(child.getId());
                child.callOnClick();
                break;
            }
        }
    }

    // If there is no vertex in front of the current vertex, disable the Forward button
    void checkCanMoveForward() {
        if (waypointsGraph.getNeighbors(currentWaypoint).get(currentDirection).equals("")) {
            forwardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.med_dark));
            forwardBtn.setEnabled(false);
        }
        else {
            forwardBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));
            forwardBtn.setEnabled(true);
        }
    }

    void setListeners() {
        leftBtn.setOnClickListener(v -> {
            //switch to image left of current image
            rotateImage(-1);
            checkCanMoveForward();
            if(isNavigation)
                getCurrentStep();
        });

        rightBtn.setOnClickListener(v -> {
            //switch to image right of current image
            rotateImage(1);
            checkCanMoveForward();
            if(isNavigation)
                getCurrentStep();
        });

        forwardBtn.setOnClickListener(v -> {
            //move the the next vertex in the graph according to the location looking at
            String nextVertex = waypointsGraph.getNextVertex(currentWaypoint, currentDirection);
            if (nextVertex != null) {
                String nextImageName = nextVertex + "-" + directions[currentDirection];
                if (waypointImages.containsKey(nextImageName)) {
//                    IndoorView.setImageBitmap(waypointImages.get(nextImageName)); TODO
                    previousWaypoints.add(currentWaypoint); //add current waypoint to the stack of previous waypoints
                    currentWaypoint = nextVertex;
                }
            }
            updateFloor();
            checkCanMoveForward();
            if(isNavigation)
                getCurrentStep();
        });

        backBtn.setOnClickListener(v -> {
            // Move to the previous waypoint according to the stack
            if(previousWaypoints.size() > 0) {
                String previousImageName = previousWaypoints.peek() + "-" + directions[currentDirection];
                if (waypointImages.containsKey(previousImageName)) {
//                    IndoorView.setImageBitmap(waypointImages.get(previousImageName)); TODO
                    currentWaypoint = previousWaypoints.pop();
                }
            }

            updateFloor();
            checkCanMoveForward();
            if(isNavigation)
                getCurrentStep();
        });

        reportBTN.setOnClickListener(v -> {
            String imageName = currentWaypoint + "-" + directions[currentDirection];
            UtilityMethods.switchActivityWithData(this, ReportActivity.class, imageName);
        });

    }

    // Rotate the image, from the current looking direction according to the given direction
    void rotateImage(int direction) {
        currentDirection += direction;
        currentDirection = (((currentDirection % 4) + 4) % 4); //why is modulus actually remainder?
        String imageName = currentWaypoint + "-" + directions[currentDirection];
        if (waypointImages.containsKey(imageName)) {
            if (direction == 1) {
                viewFlipper.setInAnimation(this, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                viewFlipper.showNext();
            }
            else {
                viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
                viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
                viewFlipper.showPrevious();
            }
        }
    }

    void findViews() {
        floorPlan = findViewById(R.id.Navigation_IMG_Floor);
        backBtn = findViewById(R.id.Navigation_IMGBTN_back);
        leftBtn = findViewById(R.id.Navigation_IMGBTN_left);
        forwardBtn = findViewById(R.id.Navigation_IMGBTN_up);
        rightBtn = findViewById(R.id.Navigation_IMGBTN_right);
        reportBTN = findViewById(R.id.Navigation_FAB_report);
        toggleGroup = findViewById(R.id.Navigation_MBTG_navigation);
        IndoorViews = new ImageView[]{findViewById(R.id.Navigation_IMG_Indoor_up),
                findViewById(R.id.Navigation_IMG_Indoor_right),
                findViewById(R.id.Navigation_IMG_Indoor_down),
                findViewById(R.id.Navigation_IMG_Indoor_left)};
        viewFlipper = findViewById(R.id.Navigation_VF_Indoor);
    }
}