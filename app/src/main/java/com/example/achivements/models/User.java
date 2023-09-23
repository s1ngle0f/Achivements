package com.example.achivements.models;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
//@ToString
public class User implements Serializable {
    private int id = 0;
    private String username;
    private String password;
    private String description;

    private boolean active = true;

    private List<Achivement> achivements = new ArrayList<>();

    private Set<User> friends = new HashSet<>();
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, String description, boolean active, List<Achivement> achivements, Set<User> friends, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.description = description;
        this.active = active;
        this.achivements = achivements;
        this.friends = friends;
        this.roles = roles;
    }

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

    public Achivement getActiveAchivement() {
        if(!achivements.isEmpty())
            return achivements.get(achivements.size()-1);
        return null;
    }

    public User clearFriendsRecursive(){
        if(friends != null)
            for (User user : friends)
                user.setFriends(null);
        return this;
    }

    public User cloneWithoutFriends(){
        return new User(id, username, password, description, active, achivements, null, roles);
    }

    @Override
    public String toString() {
        String friendsString = "[";
        if(friends != null)
            for (User user : friends)
                friendsString += "\n\t" + user.toString(2);
        friendsString += "]";
        return "User{" +
                "\n\tid=" + id +
                ",\n\tusername='" + username + '\'' +
                ",\n\tpassword='" + password + '\'' +
                ",\n\tdescription='" + description + '\'' +
                ",\n\tactive=" + active +
                ",\n\tachivements=" + achivements +
                ",\n\troles=" + roles +
                ",\n\tfriends=" + friendsString +
                "\n}";
    }

    public String toString(int countTab) {
        String userEndTab = "\t".repeat(countTab - 1);
        String tab = "\t".repeat(countTab);
        String friendsString = "[";

        friendsString += "]";
        return "User{" +
                "\n" + tab + "id=" + id +
                ",\n" + tab + "username='" + username + '\'' +
                ",\n" + tab + "password='" + password + '\'' +
                ",\n" + tab + "description='" + description + '\'' +
                ",\n" + tab + "active=" + active +
                ",\n" + tab + "achivements=" + achivements +
                ",\n" + tab + "roles=" + roles +
                ",\n" + tab + "friends=" + friendsString +
                "\n" + userEndTab + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        boolean usernameCheck = Objects.equals(username, user.username);
        boolean idCheck = Objects.equals(id, user.id);
        return usernameCheck && idCheck;
    }

    public static List<User> getUsersForExport(List<User> users){
        List<User> res = new ArrayList<>();
        for (User user : users) {
            res.add(getUserForExport(user));
        }
        return res;
    }

    public static User getUserForExport(User user){
        User newUser = user.cloneWithoutFriends();
        newUser.setFriends(new HashSet<>());
        for (User friend : user.getFriends()) {
            newUser.addFriend(friend.cloneWithoutFriends());
        }
        return newUser;
    }
}
