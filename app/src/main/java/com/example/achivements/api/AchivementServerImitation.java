package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.ServerUser;
import com.example.achivements.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AchivementServerImitation implements IAchivementServer{
    private User user;
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Integer, String> passwords = new HashMap<>();
    private HashMap<Integer, String> accessCodes = new HashMap<>();

    public AchivementServerImitation(User user) {
        this.user = user;
        for(int i = 10; i < 30; i++){
            User _user = new User("any_user" + i, null, "any_description" + i);
            _user.AddAchivement(new Achivement("TODO Something any man" + i, "Complete", _user));
            _user.AddAchivement(new Achivement("TODO Something any man" + i, "InProgress", _user));
            users.put(i, _user);
            passwords.put(i, "123");
        }
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
        return GetUsers(count);
    }

    @Override
    public ArrayList<User> GetUsers(int count) {
        ArrayList<User> _users = new ArrayList<>();
        int counter = 0;
        for (User _user : users.values()) {
            if(counter >= count)
                break;
            _users.add(_user);
            counter++;
        }
        return _users;
    }

    @Override
    public ArrayList<User> GetUsers(String name) {
        ArrayList<User> _users = new ArrayList<>();
        for (User _user : users.values()) {
            if(_user.getLogin().contains(name))
                _users.add(_user);
        }
        return _users;
    }
}
