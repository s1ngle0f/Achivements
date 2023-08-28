package com.example.achivements.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.MainActivity;
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

        //Друзья
        RecyclerView friendsRV = root.findViewById(R.id.friends_rv);
        friendsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        FriendAdapter friendAdapter = new FriendAdapter();
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));
        friendAdapter.AddFriend(new Friend("123123", "Blablablabla", "Online"));

        friendsRV.setAdapter(friendAdapter);
        //!Друзья

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Активная(текущая) задача
        LinearLayout activeAchivement = view.findViewById(R.id.current_achivement);
        activeAchivement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("achivementText", "Some text for test bundle");
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_achivementFragment, bundle);
            }
        });
        //
        if(MainActivity.BottomNV != null)
            MainActivity.BottomNV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}