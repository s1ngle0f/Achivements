package com.example.achivements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.example.achivements.api.ServerApi;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.Status;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;
public static SharedPreferences sharedPreferences;
public static SharedPreferences.Editor editor;
public static BottomNavigationView BottomNV;
private Executor executor = Executors.newSingleThreadExecutor();
//public static User user = createUserInstance();
public static User user = null;
public static ServerApi serverApi; //При работе с реальным бэком вернуть интерфейс
public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("START MAINACTIVITY");

        Bundle args = getIntent().getExtras();

        if(sharedPreferences == null){
            sharedPreferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String jwt = sharedPreferences.getString("jwt", null);
            if(jwt != null)
                ServerApi.setJwt(jwt);
        }

        if(serverApi == null)
            serverApi = new ServerApi();

        if(user == null)
            CompletableFuture.supplyAsync(() -> serverApi.getUserByAuth(), executor)
                    .thenAccept(_user -> {
                        MainActivity.user = _user;
                    });
        else {
            Status statusLastAchivement = user.getActiveAchivement().getStatus();
            if (statusLastAchivement != Status.ACTIVE) {
                CompletableFuture.supplyAsync(() -> serverApi.getNewAchivement(statusLastAchivement), executor)
                        .thenAccept(_user -> {
                            MainActivity.user = _user;
                        });
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mainActivity = this;
        /////////////////////////////////////
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNV = navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.searchFriendsFragment, R.id.accountFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}