package com.example.achivements.models;

public class Friend {
    private String login;
    private String activeAchivement;
    private String status;

    public Friend(String login, String activeAchivement, String status) {
        this.login = login;
        this.activeAchivement = activeAchivement;
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getActiveAchivement() {
        return activeAchivement;
    }

    public void setActiveAchivement(String activeAchivement) {
        this.activeAchivement = activeAchivement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
