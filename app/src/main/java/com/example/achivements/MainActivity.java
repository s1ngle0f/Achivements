package com.example.achivements;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;
public static BottomNavigationView BottomNV;
public static User user = createUserInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

    private static User createUserInstance(){
        User _user = new User("some_login", "some_password", "some_description");
        //Создание друзей
        ArrayList<User> friends = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            User _friend = new User("friend_login"+i, "friend_password"+i, "friend_description"+i);
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, "Complete", _friend));
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, "Complete", _friend));
            _friend.AddAchivement(new Achivement("TODO Something friend" + i, "In progress", _friend));
            friends.add(_friend);
        }
        //!Создание друзей
        ArrayList<Achivement> achivements = new ArrayList<>();
        achivements.add(new Achivement("TODO MYSELF1", "Complete", _user));
        achivements.add(new Achivement("TODO MYSELF2", "Complete", _user));
        achivements.add(new Achivement("TODO MYSELF3", "Complete", _user));
        achivements.add(new Achivement("TODO MYSELF4", "In progress", _user));

        _user.setAchivements(achivements);
        _user.setFriends(friends);

        return _user;
    }
}