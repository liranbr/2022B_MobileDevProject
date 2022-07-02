package com.example.mobiledevproject.Objects;

import com.google.type.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Location implements Serializable {

    String LocationName;
    HashMap<String, List<String>> Waypoints = new HashMap<>();
    HashMap<String,String> POIs = new HashMap<>(); //POI name -> waypoint ID
    HashMap<String, String> WaypointToFloor = new HashMap<>(); //waypoint ID -> floor ID
    String Floors;
    String Entrance_LatLng;
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

    public HashMap<String, String> getWaypointToFloor() { return WaypointToFloor; }

    public String getFloors() { return Floors; }

    public String getEntrance_LatLng() { return Entrance_LatLng; }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public void setWaypoints(HashMap<String,List<String>> waypoints) {
        this.Waypoints = waypoints;
    }

    public void setPOIs(HashMap<String,String> POIs) {
        this.POIs = POIs;
    }

    public void setWaypointToFloor(HashMap<String, String> waypointToFloor) { this.WaypointToFloor = waypointToFloor; }

    public void setFloors(String floors) { this.Floors = floors; }

    public void setEntrance_LatLng(String entrance_LatLng) { this.Entrance_LatLng = entrance_LatLng; }

}
