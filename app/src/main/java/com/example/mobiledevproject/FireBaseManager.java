package com.example.mobiledevproject;

import android.net.Uri;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.mobiledevproject.Objects.Location;
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
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void getLocation(String locationName, AutoCompleteTextView acv, Location loc) {
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


                loc.setLocationName(location.getLocationName());
                loc.setPOIs(poisMap);
                loc.setWaypoints(location.getWaypoints());
            }
        });
    }


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


}
