package com.example.achivements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.example.achivements.api.AchivementServerImitation;
import com.example.achivements.api.IAchivementServer;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.achivements.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;
public static SharedPreferences sharedPreferences;
public static SharedPreferences ServerSharedPreferences;
public static SharedPreferences.Editor editor;
public static BottomNavigationView BottomNV;
//public static User user = createUserInstance();
public static User user = null;
public static AchivementServerImitation ServerEmulator; //При работе с реальным бэком вернуть интерфейс
public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("START MAINACTIVITY");

        Bundle args = getIntent().getExtras();

        if(sharedPreferences == null){
            sharedPreferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        if(ServerSharedPreferences == null)
            ServerSharedPreferences = getSharedPreferences("serverSettings", Context.MODE_PRIVATE);

        if(ServerEmulator == null)
            ServerEmulator = new AchivementServerImitation(user);

        if(user == null)
            user = ServerEmulator.GetUserInfo(sharedPreferences.getString("login", ""), sharedPreferences.getString("accessToken", ""));
        else
            if(user.GetActiveAchivement().getStatus() != Achivement.Status.ACTIVE)
            {
                Achivement newAchivement = MainActivity.ServerEmulator.GetNewAchivement();
                newAchivement.setUser(user);
                user.AddAchivement(newAchivement);
                MainActivity.ServerEmulator.EditUser(user);
            }
        System.out.println(sharedPreferences.getString("login", "") + " " + sharedPreferences.getString("accessToken", ""));
        System.out.println(user);
        System.out.println(ServerSharedPreferences.getString("test", "not test"));

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mainActivity = this;
        /////////////////////////////////////
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNV = navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.searchFriendsFragment, R.id.accountFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("!onResume!");
        System.out.println(user);
        if(sharedPreferences != null)
            if(sharedPreferences.contains("user")){
                String userJson = sharedPreferences.getString("user", "");
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("!onPause!");
        System.out.println(user);
        if(user!=null)
        {
            editor.putString("userLogin", user.getLogin());
            editor.putString("userAccessToken", user.getAccessToken());
            editor.apply();
        }
    }

    @Override
    protected void onStop() {
        ServerEmulator.Destructor();
        System.out.println("STOP");
        super.onStop();
    }

    private static User createUserInstance(){
        User _user = new User("some_login", "some_password", "some_description");
        //Создание друзей
        ArrayList<User> friends = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            User _friend = new User("friend_login"+i, "friend_password"+i, "friend_description"+i);
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, Achivement.Status.COMPLETED, _friend));
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, Achivement.Status.FAILED, _friend));
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, Achivement.Status.ACTIVE, _friend));
            friends.add(_friend);
        }
        //!Создание друзей
        ArrayList<Achivement> achivements = new ArrayList<>();
        achivements.add(new Achivement("TODO MYSELF1", Achivement.Status.COMPLETED, _user));
        achivements.add(new Achivement("TODO MYSELF2", Achivement.Status.COMPLETED, _user));
        achivements.add(new Achivement("TODO MYSELF3", Achivement.Status.FAILED, _user));
        achivements.add(new Achivement("TODO MYSELF4", Achivement.Status.ACTIVE, _user));

        _user.setAchivements(achivements);
        _user.setFriends(friends);

        return _user;
    }
}