package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.util.ArrayList;
import java.util.List;

public class AchivementServer implements IAchivementServer {
    private User user;
    private String accessCode;

    public AchivementServer(User user) {
        this.user = user;
    }

    public boolean IsExistUser()
    {
        return false;
    }

    public boolean IsExistUser(User user)
    {
        return false;
    }

    public void CreateUser(User user)
    {

    }

    public Achivement GetNewAchivement()
    {
        return new Achivement();
    }

    public ArrayList<Achivement> GetAchivements(int count)
    {
        return new ArrayList<Achivement>();
    }

    public String GetAccessCode()
    {
        return "";
    }

    public void EditAchivement(Achivement achivement)
    {

    }

    public void EditUser(User user)
    {

    }

    @Override
    public ArrayList<User> GetUsers() {
        return null;
    }

    @Override
    public ArrayList<User> GetUsers(int count) {
        return null;
    }
}
