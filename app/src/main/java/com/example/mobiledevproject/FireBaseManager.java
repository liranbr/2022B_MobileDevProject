package com.example.mobiledevproject;

import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.mobiledevproject.Objects.Location;
import com.example.mobiledevproject.Objects.Waypoint;
import com.example.mobiledevproject.Utility.AutoSuggestAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FireBaseManager {

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void readLocations(List<String> locations) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("locations");
        ref.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                List<String> temp = task.getResult().toObjects(String.class);
                locations.addAll(temp);
            }
        });
    }

    public static Task<Void> uploadLocation(Location location, String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("locations").document(name).set(location);
    }

    public static void getLocation(String locationName, AutoCompleteTextView acv, List<String> tmp, List<Waypoint> waypoints) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").document(locationName).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Location location = task.getResult().toObject(Location.class);
                HashMap<String,String> poisMap = location.getPOIs();
                List<String> pois = new ArrayList<>();
                for(String poi : poisMap.keySet()) {
                    pois.add(locationName + ", " + poi);
                }
                tmp.addAll(pois);
                acv.setAdapter(new AutoSuggestAdapter(acv.getContext(),android.R.layout.simple_list_item_1, pois));
                acv.setThreshold(3);

                HashMap<String,List<String>> wpMap = location.getWaypoints();
                for(String wpID : wpMap.keySet()) {
                    waypoints.add(new Waypoint(wpID, Objects.requireNonNull(wpMap.get(wpID)).toArray(new String[0])));
                }
            }
        });
    }

}
