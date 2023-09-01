package com.example.achivements.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.achivements.MainActivity;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.Comment;
import com.example.achivements.models.ServerUser;
import com.example.achivements.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        Gson gson = new Gson();
        HashMap<Integer, User> _users =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("users", ""),
                        new TypeToken<HashMap<Integer, User>>(){}.getType());

        HashMap<Integer, String> _passwords =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("passwords", ""),
                        new TypeToken<HashMap<Integer, String>>(){}.getType());

        HashMap<Integer, ArrayList<String>> _accessCodes =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("accessCodes", ""),
                        new TypeToken<HashMap<Integer, ArrayList<String>>>(){}.getType());
        if(_users != null) {
            users = _users;
            if(_passwords != null)
                passwords = _passwords;
            if(_accessCodes != null)
                accessCodes = _accessCodes;
            for (User _user : users.values()) {
                _user.ResetFriends(GetUsers(_user.getFriendIds()));
//                _user.ResetFriends();
            }
        }
        else{
            System.out.println("DATA BASED CREATED TRY!!!");
            passwords.clear();
            accessCodes.clear();
            for(int i = 10; i < 30; i++){
                User _user = new User("u" + i, null, "any_description" + i);
                _user.AddAchivement(new Achivement("TODO Something any man" + i, Achivement.Status.COMPLETED, _user));
                _user.AddAchivement(new Achivement("TODO Something any man" + i, Achivement.Status.ACTIVE, _user).AddComment(new Comment(_user.getId(), "Some comment")));
                users.put(_user.getId(), _user);
                passwords.put(_user.getId(), "123");
            }
        }
        System.out.println(users.size());
    }

    public void Destructor(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = MainActivity.mainActivity.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("users", gson.toJson(users));
        editor.putString("passwords", gson.toJson(passwords));
        editor.putString("accessCodes", gson.toJson(accessCodes));
        editor.putString("test", "debil");
        editor.apply();
    }
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
    }

    public HashMap<Integer, String> getPasswords() {
        return passwords;
    }

    public void setPasswords(HashMap<Integer, String> passwords) {
        this.passwords = passwords;
    }

    public HashMap<Integer, ArrayList<String>> getAccessCodes() {
        return accessCodes;
    }

    public void setAccessCodes(HashMap<Integer, ArrayList<String>> accessCodes) {
        this.accessCodes = accessCodes;
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

    public ArrayList<User> GetUsers(ArrayList<Integer> ids) {
        ArrayList<User> _users = new ArrayList<>();
        for (int id : ids) {
            if(users.get(id) != null)
                _users.add(users.get(id));
        }
        return _users;
    }

    public User GetUser(int id) {
        return users.get(id);
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
