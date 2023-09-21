package com.example.achivements.models;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Achivement implements Serializable {
    private int id = 0;
    private String text;
    private Status status = Status.ACTIVE;
    private String image;
    private Set<Comment> comments = new HashSet<>();
    private User user;

    public void addComment(Comment comment){
        boolean isContainComment = comments.stream().anyMatch(_comment -> _comment.getId() == comment.getId());
        if(!isContainComment)
            comments.add(comment);
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
    @Override
    public String toString() {
        return "Achivement{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
