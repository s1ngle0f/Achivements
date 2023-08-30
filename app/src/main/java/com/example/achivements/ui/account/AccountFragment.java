package com.example.achivements.ui.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.databinding.FragmentAccountBinding;
import com.example.achivements.databinding.FragmentHomeBinding;

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
        ImageButton settingsButton = root.findViewById(R.id.account_settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSelfAccount", true);
                Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_editAccountFragment, bundle);
            }
        });
        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.VISIBLE);
        if(args != null) {
            if (args.getBoolean("isSelfAccount", true))
                root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
            else
                root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
        }else {
            root.findViewById(R.id.account_subscribe_button).setVisibility(View.GONE);
//            root.findViewById(R.id.account_settings_button).setVisibility(View.GONE);
        }
        return root;
    }
}