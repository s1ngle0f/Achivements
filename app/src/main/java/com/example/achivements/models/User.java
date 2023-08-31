package com.example.achivements.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static int count = 0;
    private int id;
    private String login, accessToken, description;
    private ArrayList<Achivement> achivements = new ArrayList<>();
    private ArrayList<User> friends = new ArrayList<>();

    public User(int id, String login, String accessToken, String description, ArrayList<Achivement> achivements, ArrayList<User> friends) {
        this.id = id;
        this.login = login;
        this.accessToken = accessToken;
        this.description = description;
        this.achivements = achivements;
        this.friends = friends;
    }

    public User(String login, String accessToken, String description, ArrayList<Achivement> achivements, ArrayList<User> friends) {
        this.id = count++;
        this.login = login;
        this.accessToken = accessToken;
        this.description = description;
        this.achivements = achivements;
        this.friends = friends;
    }

    public User(String login, String accessToken, String description, ArrayList<Achivement> achivements) {
        this.id = count++;
        this.login = login;
        this.accessToken = accessToken;
        this.description = description;
        this.achivements = achivements;
    }

    public User(int id, String login, String accessToken, String description) {
        this.id = id;
        this.login = login;
        this.accessToken = accessToken;
        this.description = description;
    }

    public User(String login, String accessToken, String description) {
        this.id = count++;
        this.login = login;
        this.accessToken = accessToken;
        this.description = description;
    }

    public User(int id) {
        this.id = id;
    }

    public User(){
        this.id = count++;
    }

    public Achivement GetActiveAchivement(){
        if(achivements.size() > 0)
            return achivements.get(achivements.size()-1);
        return null;
    }

    public void AddAchivement(Achivement achivement){
        achivements.add(achivement);
    }

    public void AddAchivement(ArrayList<Achivement> achivements){
        for (Achivement achivement : achivements) {
            this.achivements.add(achivement);
        }
    }

    public void AddFriend(User friend){
        friends.add(friend);
    }

    public void AddFriend(ArrayList<User> friends){
        for (User friend : friends) {
            this.friends.add(friend);
        }
    }

    public void DeleteFriend(User friend){
        friends.remove(friend);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAchivements(ArrayList<Achivement> achivements) {
        this.achivements = achivements;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<Achivement> getAchivements() {
        return achivements;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    @NonNull
    @Override
    public User clone() {
        ArrayList<Achivement> _achivements = new ArrayList<>();
        ArrayList<User> _friends = new ArrayList<>();
        for (User user : friends) {
            _friends.add(user);
        }
        for (Achivement achivement : achivements) {
            _achivements.add(achivement);
        }
        User user = new User(login, accessToken, description, _achivements, _friends);
        return user;
    }
}
