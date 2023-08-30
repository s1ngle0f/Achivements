package com.example.achivements.models;

public class Achivement {
    private double count = 0;
    private double id;
    private String text;
    private String status;

    public Achivement () {}

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
}
