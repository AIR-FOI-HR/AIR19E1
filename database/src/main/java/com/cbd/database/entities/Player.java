package com.cbd.database.entities;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Player {

    private String name, photoLink, phone;

    public Player() {

    }

    public Player(String name, String photoLink, String phone) {
        this.name = name;
        this.photoLink = photoLink;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
