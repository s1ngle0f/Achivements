package com.example.achivements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.example.achivements.api.ServerApi;
import com.example.achivements.db.LocalDatabase;
import com.example.achivements.db.model.AchivementImage;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.Status;
import com.example.achivements.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

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
public static LocalDatabase database;
public static SharedPreferences.Editor editor;
public static BottomNavigationView BottomNV;
private Executor executor = Executors.newSingleThreadExecutor();
public static User user = null;
public static ServerApi serverApi;
public static MainActivity mainActivity;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();

        getPermissions();

        createLocalDatabase();

        gettingFromSharedPreferences();

        creatingUserAndServer();

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

    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void createLocalDatabase() {
        if(database == null){
            database = Room.databaseBuilder(this, LocalDatabase.class, HelpFunctions.databaseName)
                    .allowMainThreadQueries() // Позволяет выполнять запросы на главном потоке (для демонстрации, не рекомендуется в реальном приложении)
                    .build();
        }
    }

    private void creatingUserAndServer(){
        if(serverApi == null)
            serverApi = new ServerApi();

        if(user == null) {
            CompletableFuture.supplyAsync(() -> serverApi.getUserByAuth(), executor)
                    .thenAccept(_user -> {
                        MainActivity.user = _user;
                        if(user.getActiveAchivement() == null)
                            CompletableFuture.supplyAsync(() -> serverApi.getNewAchivement(Status.COMPLETED), executor)
                                    .thenAccept(__user -> {
                                        MainActivity.user = __user;
                                    });
                    });
        }
        else {
            Achivement activeAchivement = user.getActiveAchivement();
            if(activeAchivement != null) {
                Status statusLastAchivement = activeAchivement.getStatus();
                if (statusLastAchivement != Status.ACTIVE) {
                    CompletableFuture.supplyAsync(() -> serverApi.getNewAchivement(statusLastAchivement), executor)
                            .thenAccept(_user -> {
                                MainActivity.user = _user;
                            });
                }
            }else{
                CompletableFuture.supplyAsync(() -> serverApi.getNewAchivement(Status.COMPLETED), executor)
                        .thenAccept(_user -> {
                            MainActivity.user = _user;
                        });
            }
        }
    }

    private void gettingFromSharedPreferences(){
        if(sharedPreferences == null){
            sharedPreferences = getSharedPreferences(HelpFunctions.nameSharedPreferences, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String jwt = sharedPreferences.getString(HelpFunctions.jwt, "");
            if(!jwt.isEmpty())
                ServerApi.setJwt(jwt);
            CompletableFuture.supplyAsync(() -> serverApi.validJwt(), executor)
                    .thenAccept(_isValid -> {
                        if(!_isValid){
                            MainActivity.editor.clear();
                            MainActivity.editor.apply();
                            MainActivity.user = null;
                            ServerApi.setJwt("");
                            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(myIntent);
                        }
                    });
        }
    }

    public boolean checkCameraPermission() {
        int cameraPermission = PackageManager.PERMISSION_GRANTED;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            cameraPermission = checkSelfPermission(android.Manifest.permission.CAMERA);
        }
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }
}