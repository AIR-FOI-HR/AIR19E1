package com.cbd.database.entities;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Collection;
import java.util.List;

@IgnoreExtraProperties
public class Venue {

    private String uid;

    private String name;
    private Double latitude;
    private Double longitude;
    private List<Activity> activities;

    public Venue() {

    }

    public Venue(String name, Double latitude, Double longitude, List<Activity> activities) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.activities = activities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Collection<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
