package com.example.achivements.models;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private int id = 0;
    private String username;
    private String password;
    private String description;

    private boolean active = true;

    private List<Achivement> achivements = new ArrayList<>();

    private Set<User> friends = new HashSet<>();
    private Set<Role> roles = new HashSet<>();

    public void addFriend(User user){
        if (!friends.stream().anyMatch(friend -> user.getId() == friend.getId()))
            friends.add(user);
    }

    public void removeFriend(User user){
        friends.removeIf(friend -> user.getId() == friend.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Achivement> getAchivements() {
        return achivements;
    }

    public void setAchivements(List<Achivement> achivements) {
        this.achivements = achivements;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
