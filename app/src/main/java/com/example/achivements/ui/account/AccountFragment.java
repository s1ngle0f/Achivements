package com.example.achivements.ui.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.achivements.LoginActivity;
import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.SettingsActivity;
import com.example.achivements.adapters.AchivementAdapter;
import com.example.achivements.databinding.FragmentAccountBinding;
import com.example.achivements.databinding.FragmentHomeBinding;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AccountFragment extends Fragment {
    private Executor executor = Executors.newSingleThreadExecutor();
    private FragmentAccountBinding binding;
    View root;
    Bundle args;
    TextView accountLogin;
    TextView accountDescription;
    ImageButton settingsButton;
    ImageView avatar;
    Button subscribeButton;
    RecyclerView accountLastAchivementsRV;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        args = getArguments();
        User user = getUserAccount();
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        init();
        if(user != null){
            buttonListeners(user);
            chooseAccountMode(user);
            loadAvatar(user);
            putIntoTemplate(user);
        }else{
            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(myIntent);
        }
        return root;
    }

    private void init(){
        root = binding.getRoot();
        accountLogin = root.findViewById(R.id.account_login);
        accountDescription = root.findViewById(R.id.account_descripton);
        settingsButton = root.findViewById(R.id.account_settings_button);
        avatar = root.findViewById(R.id.account_avatar);
        subscribeButton = root.findViewById(R.id.account_subscribe_button);
        accountLastAchivementsRV = root.findViewById(R.id.account_achivement_rv);
        accountLastAchivementsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.VISIBLE);
    }

    private void buttonListeners(User user){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(myIntent);
            }
        });
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.user == null){
                    Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(myIntent);
                }else{
                    if(MainActivity.user.getFriends().stream().anyMatch(user1 -> user1.equals(user))){
                        MainActivity.user.removeFriend(user);
                        subscribeButton.setText("Подписаться");
                    }else{
                        MainActivity.user.addFriend(user);
                        subscribeButton.setText("Отписаться");
                    }

                    CompletableFuture.supplyAsync(() ->
                                    MainActivity.serverApi.editUser(MainActivity.user), executor)
                            .thenAccept(_user -> {
                                MainActivity.user = _user;
                            });
                }
            }
        });
    }

    private void chooseAccountMode(User user){
        if(user != null && user.equals(MainActivity.user)){
            root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
            System.out.println("ACC " + user);
        }else{
            root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
            System.out.println("NOSELF ACC " + user);
        }
    }

    private User getUserAccount(){
        if(args == null) return MainActivity.user;
        if(args.getBoolean("isSelfAccount", true)) return MainActivity.user;
        return (User) args.getSerializable("account");
    }

    private void generateUserAchivements(User user) {
        if(user.getAchivements() != null){
            AchivementAdapter achivementAdapter = new AchivementAdapter(user);
            ArrayList<Achivement> reversedList = new ArrayList<>(user.getAchivements());
            Collections.reverse(reversedList);
            achivementAdapter.Add(reversedList);
            accountLastAchivementsRV.setAdapter(achivementAdapter);
        }
    }

    private void putIntoTemplate(User user){
        if(user != null) {
            accountLogin.setText(user.getUsername());
            accountDescription.setText(user.getDescription());
            if(MainActivity.user != null && MainActivity.user.getFriends() != null && MainActivity.user.getFriends().stream().anyMatch(user1 -> user1.equals(user))){
                subscribeButton.setText("Отписаться");
            }
            generateUserAchivements(user);
        }
    }

    private void loadAvatar(User user){
        if(user != null && user.equals(MainActivity.user)){
            CompletableFuture.supplyAsync(() ->
                            MainActivity.serverApi.getAvatar(), executor)
                    .thenAccept(_bytes -> {
                        if(_bytes != null){
                            Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                            getActivity().runOnUiThread(() -> {
                                avatar.setImageBitmap(photoUri);
                            });
                        }
                    });
        }else{
            int userId = user.getId();
            CompletableFuture.supplyAsync(() ->
                            MainActivity.serverApi.getAvatarById(userId), executor)
                    .thenAccept(_bytes -> {
                        if(_bytes != null){
                            Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                            getActivity().runOnUiThread(() -> {
                                avatar.setImageBitmap(photoUri);
                            });
                        }
                    });
        }
    }
}