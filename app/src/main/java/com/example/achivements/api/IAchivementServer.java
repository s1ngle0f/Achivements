package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.util.ArrayList;

public interface IAchivementServer {
    boolean IsExistUser();
    boolean IsExistUser(User user);

    void CreateUser(User user);

    Achivement GetNewAchivement();

    ArrayList<Achivement> GetAchivements(int count);

    String GetAccessCode();

    void EditAchivement(Achivement achivement);

    void EditUser(User user);
    ArrayList<User> GetUsers();
    ArrayList<User> GetUsers(int count);
}
