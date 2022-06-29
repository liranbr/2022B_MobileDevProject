package com.example.mobiledevproject.Objects;

import com.google.type.LatLng;

import java.io.Serializable;
import java.util.HashMap;

public class Location implements Serializable {

    HashMap<String,Waypoint> Waypoints = new HashMap<>();
    HashMap<String,String> POIs = new HashMap<>(); //POI name -> waypoint ID
//    LatLng entrance;

    public Location() {}

    public Location(HashMap<String,Waypoint> waypoints, HashMap<String,String> POIs, LatLng entrance) {
        this.Waypoints = waypoints;
        this.POIs = POIs;
        //this.entrance = entrance;
    }

    public HashMap<String,Waypoint> getWaypoints() {
        return Waypoints;
    }

    public HashMap<String,String> getPOIs() {
        return POIs;
    }

//    public LatLng getEntrance() {
//        return entrance;
//    }

    public void setWaypoints(HashMap<String,Waypoint> waypoints) {
        this.Waypoints = waypoints;
    }

    public void setPOIs(HashMap<String,String> POIs) {
        this.POIs = POIs;
    }

//    public void setEntrance(LatLng entrance) {
//        this.entrance = entrance;
//    }
}
