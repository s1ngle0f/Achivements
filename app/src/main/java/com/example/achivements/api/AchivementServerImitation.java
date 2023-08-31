package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.ServerUser;
import com.example.achivements.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AchivementServerImitation implements IAchivementServer{
    private User user;
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Integer, String> passwords = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> accessCodes = new HashMap<>();

    public AchivementServerImitation(User user) {
        this.user = user;
        for(int i = 10; i < 30; i++){
            User _user = new User("any_user" + i, null, "any_description" + i);
            _user.AddAchivement(new Achivement("TODO Something any man" + i, "Complete", _user));
            _user.AddAchivement(new Achivement("TODO Something any man" + i, "InProgress", _user));
            users.put(_user.getId(), _user);
            passwords.put(_user.getId(), "123");
        }
    }

    @Override
    public User GetUserInfo(String login, String accessToken) {
        for (User _user : users.values()) {
            if(accessCodes.get(_user.getId()) != null){
                if (_user.getLogin().equals(login) && accessCodes.get(_user.getId()).contains(accessToken)) {
                    return _user;
                }
            }
        }
        return null;
    }

    @Override
    public boolean IsExistUser() {
        return false;
    }

    @Override
    public boolean IsExistUser(String login, String password) {
        return false;
    }

    @Override
    public String CreateUser(String login, String password) {
        User user = new User(login);
        if(findUser(user.getLogin()) == null) {
            users.put(user.getId(), user);
            passwords.put(user.getId(), password);
            String accessToken = GetAccessCode(login, password);
            return accessToken;
        }
        return null;
    }

    private User findUser(String login){
        for (User user : users.values()) {
            if(user.getLogin().equals(login))
                return user;
        }
        return null;
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
    public String GetAccessCode(String login, String password) {
        for (User _user : users.values()) {
            if(_user.getLogin().equals(login) && passwords.get(_user.getId()).equals(password)) {
                String newToken = createToken(16);
//                _user.setAccessToken(newToken);
                if(accessCodes.get(_user.getId()) == null)
                    accessCodes.put(_user.getId(), new ArrayList<String>());
                accessCodes.get(_user.getId()).add(newToken);
                return newToken;
            }
        }
        return null;
    }

    @Override
    public void EditAchivement(Achivement achivement) {

    }

    @Override
    public void EditUser(User user) {
        if(user != null){
            users.put(user.getId(), user);
        }
    }

    @Override
    public User AddFriend(String login, String accessToken, User friend) {
        for (User _user : users.values()) {
            if(accessCodes.get(_user.getId()) != null){
                if (_user.getLogin().equals(login) && accessCodes.get(_user.getId()).contains(accessToken)) {
                    _user.AddFriend(friend);
                    return _user;
                }
            }
        }
        return null;
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

    private String createToken(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
        String res = "";
        for(int i = 0; i < length; i++){
            res += chars.toCharArray()[(int)(Math.random()*chars.length())];
        }
        return res;
    }
}
