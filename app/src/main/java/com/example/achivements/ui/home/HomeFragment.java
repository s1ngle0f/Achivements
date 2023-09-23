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
import com.example.achivements.adapters.AchivementAdapter;
import com.example.achivements.adapters.FriendAdapter;
import com.example.achivements.databinding.FragmentHomeBinding;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;
    private Executor executor = Executors.newSingleThreadExecutor();
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        System.out.println("HOME_Fragment");
        if(MainActivity.user != null) {//Друзья
            if(MainActivity.user.getFriends() != null){
                RecyclerView friendsRV = root.findViewById(R.id.friends_rv);
                friendsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

                FriendAdapter friendAdapter = new FriendAdapter();

                System.out.println("Friends: " + MainActivity.user.getFriends());
                friendAdapter.Add(MainActivity.user.getFriends().stream()
                        .collect(Collectors.toList()));

                friendsRV.setAdapter(friendAdapter);
            }
            //!Друзья

            if(MainActivity.user.getAchivements() != null){//Ачивки
                RecyclerView achivementsRV = root.findViewById(R.id.achivement_rv);
                achivementsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

                AchivementAdapter achivementAdapter = new AchivementAdapter(MainActivity.user);
                ArrayList<Achivement> reversedList = new ArrayList<>(MainActivity.user.getAchivements());
                Collections.reverse(reversedList);
                achivementAdapter.Add(reversedList);
                achivementsRV.setAdapter(achivementAdapter);

                TextView homeActiveAchivement = root.findViewById(R.id.home_active_task);
                Achivement activeAchivement = MainActivity.user.getActiveAchivement();
                if(activeAchivement != null)
                    homeActiveAchivement.setText(activeAchivement.getText());
            }
        }

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
                bundle.putSerializable("achivement", (Serializable) MainActivity.user.getActiveAchivement());
                bundle.putSerializable("owner", (Serializable) MainActivity.user);
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