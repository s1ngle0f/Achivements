package com.example.achivements.ui.achivement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.adapters.CommentAdapter;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.Comment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import carbon.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchivementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchivementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AchivementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchivementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchivementFragment newInstance(String param1, String param2) {
        AchivementFragment fragment = new AchivementFragment();
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
        View root = inflater.inflate(R.layout.fragment_achivement, container, false);

        Bundle args = getArguments();
        Achivement achivement = (Achivement) args.getSerializable("achivement");
        TextView achivementText = root.findViewById(R.id.achivement_text);
        TextView achivementLogin = root.findViewById(R.id.achivement_login);
        TextView achivementStatus = root.findViewById(R.id.achivement_status);
        TextView achivementInputField = root.findViewById(R.id.achivement_inputfield_comment);
        ImageView achivementsSender = root.findViewById(R.id.achivement_send_button);
        RecyclerView achivementCommentRV = root.findViewById(R.id.achivement_comment_rv);

        achivementCommentRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        CommentAdapter commentAdapter = new CommentAdapter();
        commentAdapter.Add(achivement.getComments());

        achivementCommentRV.setAdapter(commentAdapter);

        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.GONE);
        if(MainActivity.user == null) root.findViewById(R.id.achivement_input_comment).setVisibility(View.GONE);
        achivementText.setText(achivement.getText());
        achivementLogin.setText(achivement.getUser().getLogin());
        achivementStatus.setText("Achivement: " + achivement.getStatusText());

        achivementsSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment newComment = new Comment(MainActivity.user.getId(), achivementInputField.getText().toString());
                achivement.AddComment(newComment);
                commentAdapter.Add(newComment);
                MainActivity.ServerEmulator.EditUser(achivement.getUser());
            }
        });

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}