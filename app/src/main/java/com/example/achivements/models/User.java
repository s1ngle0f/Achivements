package com.example.achivements.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {
    private static double count = 0;
    private double id;
    private String login, password, description;
    private ArrayList<Achivement> achivements = new ArrayList<>();
    private ArrayList<User> friends = new ArrayList<>();

    public User(double id, String login, String password, String description, ArrayList<Achivement> achivements, ArrayList<User> friends) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.description = description;
        this.achivements = achivements;
        this.friends = friends;
    }

    public User(String login, String password, String description, ArrayList<Achivement> achivements, ArrayList<User> friends) {
        this.id = count++;
        this.login = login;
        this.password = password;
        this.description = description;
        this.achivements = achivements;
        this.friends = friends;
    }

    public User(String login, String password, String description, ArrayList<Achivement> achivements) {
        this.id = count++;
        this.login = login;
        this.password = password;
        this.description = description;
        this.achivements = achivements;
    }

    public User(double id, String login, String password, String description) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.description = description;
    }

    public User(String login, String password, String description) {
        this.id = count++;
        this.login = login;
        this.password = password;
        this.description = description;
    }

    public User(double id) {
        this.id = id;
    }

    public User(){
        this.id = count++;
    }

    public Achivement GetActiveAchivement(){
        return achivements.get(achivements.size()-1);
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

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        User user = new User(login, password, description, _achivements, _friends);
        return user;
    }
}
