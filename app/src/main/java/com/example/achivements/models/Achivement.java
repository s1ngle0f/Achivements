package com.example.achivements.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Achivement implements Serializable {

    public enum Status{
        ACTIVE,
        COMPLETED,
        FAILED
    }

    private int count = 0;
    private int id;
    private String text;
    private Status status = Status.ACTIVE;
    private String image;
    private ArrayList<Comment> comments = new ArrayList<>();
    private transient User user;

    public Achivement () {}


    public Achivement(int id, String text, Status status, String image, ArrayList<Comment> comments, User user) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.image = image;
        this.comments = comments;
        this.user = user;
    }

    public Achivement(String text, Status status, String image, ArrayList<Comment> comments, User user) {
        this.text = text;
        this.status = status;
        this.image = image;
        this.comments = comments;
        this.user = user;
    }

    public Achivement(int id, String text, Status status, User user) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.user = user;
    }

    public Achivement(String text, Status status, User user) {
        this.id = count++;
        this.text = text;
        this.status = status;
        this.user = user;
    }

    public Achivement(int id, String text, Status status) {
        this.id = id;
        this.text = text;
        this.status = status;
    }

    public Achivement(String text, Status status) {
        this.id = count++;
        this.text = text;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Status getStatus() {
        return status;
    }

    public String getStatusText() {
        switch (status){
            case ACTIVE:
                return "Active";
            case COMPLETED:
                return "Completed";
            case FAILED:
                return "Failed";
            default:
                return "Not stated";
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Achivement AddComment(Comment comment){
        comments.add(comment);
        return this;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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
