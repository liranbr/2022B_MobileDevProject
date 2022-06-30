package com.example.mobiledevproject.Objects;

import com.google.type.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Location implements Serializable {

    String LocationName;
    HashMap<String, List<String>> Waypoints = new HashMap<>();
    HashMap<String,String> POIs = new HashMap<>(); //POI name -> waypoint ID
//    LatLng entrance;

    public Location() {}

    public Location(HashMap<String,List<String>> waypoints, HashMap<String,String> POIs, String LocationName) {
        this.Waypoints = waypoints;
        this.POIs = POIs;
        this.LocationName = LocationName;
    }

    public Location(Location location) {
        this.LocationName = location.LocationName;
        this.Waypoints = location.Waypoints;
        this.POIs = location.POIs;
    }

    public HashMap<String,List<String>> getWaypoints() {
        return Waypoints;
    }

    public HashMap<String,String> getPOIs() {
        return POIs;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public void setWaypoints(HashMap<String,List<String>> waypoints) {
        this.Waypoints = waypoints;
    }

    public void setPOIs(HashMap<String,String> POIs) {
        this.POIs = POIs;
    }

}
