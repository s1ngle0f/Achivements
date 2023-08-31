package com.example.achivements.models;

import android.net.Uri;

import java.io.Serializable;

public class Achivement implements Serializable {
    private int count = 0;
    private int id;
    private String text;
    private String status;
    private Uri image;
    private transient User user;

    public Achivement () {}

    public Achivement(int id, String text, String status, User user) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.user = user;
    }

    public Achivement(String text, String status, User user) {
        this.id = count++;
        this.text = text;
        this.status = status;
        this.user = user;
    }

    public Achivement(int id, String text, String status) {
        this.id = id;
        this.text = text;
        this.status = status;
    }

    public Achivement(String text, String status) {
        this.id = count++;
        this.text = text;
        this.status = status;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Achivement{" +
                "count=" + count +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
