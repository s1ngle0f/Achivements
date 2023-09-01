package com.example.achivements.models;

import java.io.Serializable;

public class Comment implements Serializable {
    private int userId;
    private String text;

    public Comment(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
