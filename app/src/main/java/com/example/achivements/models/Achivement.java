package com.example.achivements.models;

import java.io.Serializable;

public class Achivement implements Serializable {
    private double count = 0;
    private double id;
    private String text;
    private String status;

    private User user;

    public Achivement () {}

    public Achivement(double id, String text, String status, User user) {
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

    public Achivement(double id, String text, String status) {
        this.id = id;
        this.text = text;
        this.status = status;
    }

    public Achivement(String text, String status) {
        this.id = count++;
        this.text = text;
        this.status = status;
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

    public double getId() {
        return id;
    }

    public void setId(double id) {
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
