package com.example.mobiledevproject.Utility;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.mobiledevproject.Objects.Graph;
import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Objects.Report;
import com.example.mobiledevproject.Objects.Waypoint;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FireBaseManager {

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();

    // Download location data from FireBase Firestore and fill the Graph with waypoints data
    public static void getLocation(String locationName, AutoCompleteTextView acv, Location loc, Graph<String> graph) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").document(locationName).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Location location = task.getResult().toObject(Location.class);
                HashMap<String,String> poisMap = location.getPOIs();
                List<String> pois = new ArrayList<>();
                for(String poi : poisMap.keySet()) {
                    pois.add(locationName + ", " + poi);
                }
                acv.setAdapter(new AutoSuggestAdapter(acv.getContext(),android.R.layout.simple_list_item_1, pois));
                acv.setThreshold(3);

                HashMap<String, List<String>> strWaypointsMap = location.getWaypoints();
                loc.setWaypoints(strWaypointsMap);
                loc.setLocationName(location.getLocationName());
                loc.setPOIs(poisMap);
                loc.setWaypointToFloor(location.getWaypointToFloor());
                loc.setFloors(location.getFloors());
                loc.setEntrance_LatLng(location.getEntrance_LatLng());

                for(String waypoint : strWaypointsMap.keySet()) {
                    List<String> neighbors = strWaypointsMap.get(waypoint);
                    graph.addVertex(waypoint);
                    for(String neigh : neighbors) {
                        graph.addEdge(waypoint, neigh, false);
                    }
                }
            }
        });
    }


    // Download a set of images from a given folder in FireBase Storage
    public static void downloadImages(String path, Consumer<String> callback) {
        StorageReference imageRef = storage.getReference().child(path);
        imageRef.listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for(StorageReference image : Objects.requireNonNull(task.getResult()).getItems()) {
                    image.getDownloadUrl().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            callback.accept(task1.getResult().toString());
                        }
                    });
                }
            }
        });
    }

    // Download a single image from FireBase Storage
    public static void downloadImage(String path, Consumer<String> callback) {
        StorageReference imageRef = storage.getReference().child(path);
        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.accept(task.getResult().toString());
            }
        });
    }

    // Upload a report to FireBase Firestore
    public static void addReport(Report rep) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports").add(rep);
    }

    // Upload an image to FireBase Storage
    public static void uploadImage(Uri uploadedImgUri, String uploadedImageName) {
        StorageReference imageRef = storage.getReference().child("report-images/" + uploadedImageName);
            imageRef.putFile(uploadedImgUri).addOnCompleteListener(task -> {
            });
    }

}

