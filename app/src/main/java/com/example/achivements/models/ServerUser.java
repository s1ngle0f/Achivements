package com.example.achivements.models;

public class ServerUser extends User{
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ServerUser(User user, String password) {
        super(user.getId(), user.getLogin(), user.getAccessToken(), user.getDescription(), user.getAchivements(), user.getFriends());
        this.password = password;
    }
}
