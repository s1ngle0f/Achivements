package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.util.ArrayList;

public interface IAchivementServer {
    boolean IsExistUser();
    boolean IsExistUser(String login, String password);

    String CreateUser(String login, String password);

    Achivement GetNewAchivement();

    ArrayList<Achivement> GetAchivements(int count);

    String GetAccessCode(String login, String password);

    void EditAchivement(Achivement achivement);

    void EditUser(User user);
    ArrayList<User> GetUsers();
    ArrayList<User> GetUsers(int count);
    ArrayList<User> GetUsers(String name);
    User GetUserInfo(String login, String accessToken);
    User AddFriend(String login, String accessToken, User friend);
}
