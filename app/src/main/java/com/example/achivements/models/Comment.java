package com.example.achivements.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    private int id = 0;
    private String text;
    private User user;
    private Achivement achivement;

    public Comment(User user, String text) {
        this.text = text;
        this.user = user;
        this.achivement = achivement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Achivement getAchivement() {
        return achivement;
    }

    public void setAchivement(Achivement achivement) {
        this.achivement = achivement;
    }

    public int getUserId(){
        return user.getId();
    }
}