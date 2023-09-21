package com.example.achivements.ui.searchFriend;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.adapters.FriendAdapter;
import com.example.achivements.databinding.FragmentSearchFriendsBinding;
import com.example.achivements.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SearchFriendsFragment extends Fragment {
    private Executor executor = Executors.newSingleThreadExecutor();
    private FragmentSearchFriendsBinding fragmentSearchFriendsBinding;

    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSearchFriendsBinding = FragmentSearchFriendsBinding.inflate(inflater, container, false);
        View root = fragmentSearchFriendsBinding.getRoot();

        EditText searcher = root.findViewById(R.id.search_friend_search_field);

        if(MainActivity.serverApi != null){//Друзья
            RecyclerView friendsRV = root.findViewById(R.id.search_friend_rv);
            friendsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

            FriendAdapter friendAdapter = new FriendAdapter();
            CompletableFuture.supplyAsync(() -> MainActivity.serverApi.getUsers(), executor)
                    .thenAccept(users -> {
                        getActivity().runOnUiThread(() -> {
                            friendAdapter.Set((ArrayList<User>) users);
                        });
                    });

            friendsRV.setAdapter(friendAdapter);
            //!Друзья
            searcher.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    CompletableFuture.supplyAsync(() ->
                                    MainActivity.serverApi.getUsersByUsername(searcher.getText().toString()), executor)
                            .thenAccept(users -> {
                                getActivity().runOnUiThread(() -> {
                                    friendAdapter.Set((ArrayList<User>) users);
                                });
                            });
                }
            });
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}