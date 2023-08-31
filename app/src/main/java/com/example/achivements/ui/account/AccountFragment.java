package com.example.achivements.ui.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.adapters.AchivementAdapter;
import com.example.achivements.databinding.FragmentAccountBinding;
import com.example.achivements.databinding.FragmentHomeBinding;
import com.example.achivements.models.User;

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
        RecyclerView accountLastAchivementsRV = root.findViewById(R.id.account_achivement_rv);
        accountLastAchivementsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSelfAccount", true);
                bundle.putSerializable("account", MainActivity.user);
                Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_editAccountFragment, bundle);
            }
        });
        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.VISIBLE);
        User user = null;
        if(args != null) {
            if (args.getBoolean("isSelfAccount", true)) {
                root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
                user = MainActivity.user;
            }
            else{
                root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
                user = (User) args.getSerializable("account");
            }
        }
        else {
            root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
            user = MainActivity.user;
        }
        if(user != null) {
            accountLogin.setText(user.getLogin());
            accountDescription.setText(user.getDescription());
            AchivementAdapter achivementAdapter = new AchivementAdapter();
            achivementAdapter.Add(user.getAchivements());
            accountLastAchivementsRV.setAdapter(achivementAdapter);
        }else{
            root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
        }
        return root;
    }
}