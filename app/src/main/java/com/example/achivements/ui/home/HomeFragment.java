package com.example.achivements.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.R;
import com.example.achivements.adapters.FriendAdapter;
import com.example.achivements.databinding.FragmentHomeBinding;
import com.example.achivements.models.Friend;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView friendsRV = root.findViewById(R.id.friends_rv);
        friendsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        FriendAdapter friendAdapter = new FriendAdapter();
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));

        friendsRV.setAdapter(friendAdapter);

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}