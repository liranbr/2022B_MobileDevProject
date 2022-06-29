package com.example.mobiledevproject.Objects;

import com.google.type.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Location implements Serializable {

    HashMap<String, List<String>> Waypoints = new HashMap<>();
    HashMap<String,String> POIs = new HashMap<>(); //POI name -> waypoint ID
//    LatLng entrance;

    public Location() {}

    public Location(HashMap<String,List<String>> waypoints, HashMap<String,String> POIs, LatLng entrance) {
        this.Waypoints = waypoints;
        this.POIs = POIs;
        //this.entrance = entrance;
    }

    public HashMap<String,List<String>> getWaypoints() {
        return Waypoints;
    }

    public HashMap<String,String> getPOIs() {
        return POIs;
    }

//    public LatLng getEntrance() {
//        return entrance;
//    }

    public void setWaypoints(HashMap<String,List<String>> waypoints) {
        this.Waypoints = waypoints;
    }

    public void setPOIs(HashMap<String,String> POIs) {
        this.POIs = POIs;
    }

//    public void setEntrance(LatLng entrance) {
//        this.entrance = entrance;
//    }
}
