package com.example.mobiledevproject.Objects;

import java.io.Serializable;

public class Report implements Serializable {

    String reporterEmail;
    String text;
    String uploadedImgName;
    String waypointId;
    String direction;

    public Report() {}

//    public Report(String reporterEmail, String text, String imageName, String waypointId) {
//        this.reporterEmail = reporterEmail;
//        this.text = text;
//        this.imageName = imageName;
//        this.waypointId = waypointId;
//    }

    //setter
    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageName(String imageName) {
        this.uploadedImgName = imageName;
    }

    public void setWaypointId(String waypointId) {
        this.waypointId = waypointId;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    //getter
    public String getReporterEmail() {
        return reporterEmail;
    }

    public String getText() {
        return text;
    }

    public String getImageName() {
        return uploadedImgName;
    }

    public String getWaypointId() {
        return waypointId;
    }

    public String getDirection() {
        return direction;
    }
}
