package com.example.achivements.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle args = getArguments();
        TextView accountLogin = root.findViewById(R.id.account_login);
        TextView accountDescription = root.findViewById(R.id.account_descripton);
        ImageButton settingsButton = root.findViewById(R.id.account_settings_button);
        ImageView avatar = root.findViewById(R.id.account_avatar);
        Button subscribeButton = root.findViewById(R.id.account_subscribe_button);
        RecyclerView accountLastAchivementsRV = root.findViewById(R.id.account_achivement_rv);
        accountLastAchivementsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(myIntent);
            }
        });
        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.VISIBLE);
        User user = null;
        if(args != null) {
            if (args.getBoolean("isSelfAccount", true)) {
                root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
                user = MainActivity.user;
                String projectFolderPath = MainActivity.mainActivity.getApplicationContext().getFilesDir() + "/project/";
                String imageName = "avatar.jpg"; // Change the image name as needed
                File avatarImageFile = new File(projectFolderPath + imageName);
                if(avatarImageFile.exists())
                    avatar.setImageURI(Uri.fromFile(avatarImageFile));
                System.out.println("ISSELF ACC " + user);
            }
            else{
                root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
                user = (User) args.getSerializable("account");
                if(MainActivity.user == user) {
                    root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
                    root.findViewById(R.id.account_settings_button).setVisibility(View.VISIBLE);
                }
                System.out.println("NOSELF ACC " + user);
            }
        }
        else {
            root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
            user = MainActivity.user;
            String projectFolderPath = MainActivity.mainActivity.getApplicationContext().getFilesDir() + "/project/";
            String imageName = "avatar.jpg"; // Change the image name as needed
            File avatarImageFile = new File(projectFolderPath + imageName);
            if(avatarImageFile.exists())
                avatar.setImageURI(Uri.fromFile(avatarImageFile));
            System.out.println("ACC " + user);
        }
        if(user != null) {
            accountLogin.setText(user.getLogin());
            accountDescription.setText(user.getDescription());
            if(MainActivity.user != null && MainActivity.user.getFriends() != null && MainActivity.user.getFriends().contains(user)){
                subscribeButton.setText("Отписаться");
            }
            User finalUser = user;
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MainActivity.user == null){
                        Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(myIntent);
                    }else{
                        if(MainActivity.user.getFriends().contains(finalUser)){
                            subscribeButton.setText("Отписаться");
                            MainActivity.user.DeleteFriend(finalUser);
                        }else{
                            subscribeButton.setText("Подписаться");
                            MainActivity.user.AddFriend(finalUser);
                        }

                        MainActivity.ServerEmulator.EditUser(MainActivity.user);
                    }
                }
            });
            if(user.getAchivements() != null){
                AchivementAdapter achivementAdapter = new AchivementAdapter();
                ArrayList<Achivement> reversedList = new ArrayList<>(user.getAchivements());
                Collections.reverse(reversedList);
                achivementAdapter.Add(reversedList);
                accountLastAchivementsRV.setAdapter(achivementAdapter);
            }
        }else{
            root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(myIntent);
        }
        return root;
    }
}