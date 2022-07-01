package com.example.mobiledevproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mobiledevproject.Objects.Graph;
import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

    int currentDirection = 0; // 0 = up, 1 = right, 2 = down, 3 = left
    String fromWhere = "";
    String fromWhereId = "";
    String currentWaypoint = "";
    String previousWaypoint = "";
    String destination = "";
    String destinationId = "";
    String[] directions = {"up", "right", "down", "left"};

    HashMap<String, Bitmap> waypointImages = new HashMap<>();

    Location loc = MainActivity.getLocation();
    Graph<String> waypointsGraph = MainActivity.getGraph();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_screen);
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromWhere = extras.getString("key0");
            if (extras.containsKey("key1")) {
                destination = extras.getString("key1");
            }
            fromWhereId = loc.getPOIs().get(fromWhere);
            currentWaypoint = fromWhereId;
            destinationId = loc.getPOIs().get(destination);
        }

        FireBaseManager.downloadImages("waypoint-images", (image) -> {
            String[] imageNameParts = image.split("\\.jpg")[0].split("%2F");
            String imageName = imageNameParts[imageNameParts.length - 1];
            Log.d("Tagu", "imageName: " + imageName + " fromWhere: " + fromWhere + " fromWhereId: " + fromWhereId);
            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            waypointImages.put(imageName, resource);
                            if (imageName.equals(currentWaypoint + "-up"))
                                IndoorView.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        });

        FireBaseManager.downloadImages("floor-plans/", (image) -> {
            String name = image.split(loc.getLocationName() + "-floor-")[1].split("\\.")[0];

            int size = Integer.parseInt(name);
            MaterialButton mb = new MaterialButton(this);
            mb.setText(name);
            mb.setTextColor(ContextCompat.getColorStateList(this, R.color.selectable_text));
            mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selectable_background));

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

        leftBtn.setOnClickListener(v -> {
            //switch to image left of current image
            rotateImage(-1);
        });

        rightBtn.setOnClickListener(v -> {
            //switch to image right of current image
            rotateImage(1);
        });

        upBtn.setOnClickListener(v -> {
            //move the the next vertex in the graph according to the location looking at
            String nextVertex = waypointsGraph.getNextVertex(currentWaypoint, currentDirection);
            if (nextVertex != null) {
                String nextImageName = nextVertex + "-" + directions[currentDirection];
                if (waypointImages.containsKey(nextImageName)) {
                    IndoorView.setImageBitmap(waypointImages.get(nextImageName));
                    previousWaypoint = currentWaypoint;
                    currentWaypoint = nextVertex;
                }
            }
        });

        backBtn.setOnClickListener(v -> {
            //move the the previous vertex in the graph according to the location looking at
            if (previousWaypoint != null) {
                String previousImageName = previousWaypoint + "-" + directions[currentDirection];
                if (waypointImages.containsKey(previousImageName)) {
                    IndoorView.setImageBitmap(waypointImages.get(previousImageName));
                    currentWaypoint = previousWaypoint;
                }
            }
        });

        reportBTN.setOnClickListener(v -> {
            UtilityMethods.switchActivity(this, ReportActivity.class);
        });

    }

    void rotateImage(int direction) {
        currentDirection += direction;
        currentDirection = (((currentDirection % 4) + 4) % 4); //why is modulus actually remainder?
        String imageName = currentWaypoint + "-" + directions[currentDirection];
        if (waypointImages.containsKey(imageName)) {
            IndoorView.setImageBitmap(waypointImages.get(imageName));
        }
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