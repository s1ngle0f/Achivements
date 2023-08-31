package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.ServerUser;
import com.example.achivements.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AchivementServerImitation implements IAchivementServer{
    private User user;
    private HashMap<Double, ServerUser> users;
    private HashMap<Double, String> accessCodes;

    public AchivementServerImitation(User user) {
        this.user = user;
    }

    @Override
    public boolean IsExistUser() {
        return false;
    }

    @Override
    public boolean IsExistUser(User user) {
        return false;
    }

    @Override
    public void CreateUser(User user, String password) {
        users.put(user.getId(), new ServerUser(user, password));
    }

    @Override
    public Achivement GetNewAchivement() {
        return null;
    }

    @Override
    public ArrayList<Achivement> GetAchivements(int count) {
        return null;
    }

    @Override
    public String GetAccessCode(User user, String password) {
        return null;
    }

    @Override
    public void EditAchivement(Achivement achivement) {

    }

    @Override
    public void EditUser(User user) {

    }

    @Override
    public ArrayList<User> GetUsers() {
        int count = 10;
        return null;
    }

    @Override
    public ArrayList<User> GetUsers(int count) {
        return null;
    }
}
