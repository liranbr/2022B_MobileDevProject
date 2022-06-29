package com.example.mobiledevproject.Objects;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Waypoint  implements Serializable {

    String id;
    String[] neighborIds = new String[4];

    public Waypoint() {}

    public Waypoint(String id, String[] neighborIds) {
        this.id = id;
        this.neighborIds = neighborIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getNeighborIds() {
        return neighborIds;
    }

    public void setNeighborIds(String[] neighborIds) {
        this.neighborIds = neighborIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "Waypoint{" +
                "id='" + id + '\'' +
                ", neighborIds=" + neighborIds +
                '}';
    }
}
