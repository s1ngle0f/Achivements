package com.example.achivements.ui.achivement;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.achivements.HelpFunctions;
import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.adapters.CommentAdapter;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.AuthentificationRequest;
import com.example.achivements.models.Comment;
import com.example.achivements.models.Status;
import com.example.achivements.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import carbon.widget.Button;
import carbon.widget.ImageView;


public class AchivementFragment extends Fragment {
    private Executor executor = Executors.newSingleThreadExecutor();
    private static final int SELECT_PICTURE = 1;

    public AchivementFragment() {
        // Required empty public constructor
    }
    private android.widget.ImageView achivementImage;
    private String imageName;
    private Uri selectedImageUri;
    Bundle args;
    TextView achivementText;
    TextView achivementLogin;
    TextView achivementStatus;
    TextView achivementInputField;
    ImageView achivementsSender;
    RecyclerView achivementCommentRV;
    Button cancelButton;
    Button acceptButton;
    android.widget.LinearLayout inputComment;
    android.widget.LinearLayout selector;
    Achivement achivement;
    User owner;
    CommentAdapter commentAdapter;

    private void init(View root){
        args = getArguments();
        achivementText = root.findViewById(R.id.achivement_text);
        achivementLogin = root.findViewById(R.id.achivement_login);
        achivementStatus = root.findViewById(R.id.achivement_status);
        achivementInputField = root.findViewById(R.id.achivement_inputfield_comment);
        achivementsSender = root.findViewById(R.id.achivement_send_button);
        achivementImage = root.findViewById(R.id.achivement_image);
        achivementCommentRV = root.findViewById(R.id.achivement_comment_rv);
        cancelButton = root.findViewById(R.id.achivement_cancel_button);
        acceptButton = root.findViewById(R.id.achivement_accept_button);
        inputComment = root.findViewById(R.id.achivement_input_comment);
        selector = root.findViewById(R.id.achivement_selector);
        achivement = (Achivement) args.getSerializable("achivement");
        owner = (User) args.getSerializable("owner");

        achivementCommentRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        commentAdapter = new CommentAdapter();
        commentAdapter.Add((ArrayList<Comment>) achivement.getComments().stream().collect(Collectors.toList()));
        achivementCommentRV.setAdapter(commentAdapter);
        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achivement, container, false);

        init(root);

        loadAchivementImage();
        if(MainActivity.user == null) {
            inputComment.setVisibility(View.GONE);
            selector.setVisibility(View.GONE);
        }
        else{
            if(MainActivity.user.getAchivements().stream().anyMatch(_achivement -> _achivement.getId() == achivement.getId()) &&
                    achivement.getStatus() == Status.ACTIVE){
                visibleForActiveAchivement();
            }else{
                visibleForFinishedAchivement();
            }
        }
        putIntoTemplate();

        return root;
    }

    private void putIntoTemplate() {
        achivementText.setText(achivement.getText());
        achivementLogin.setText(owner.getUsername());
        achivementStatus.setText("Achivement: " + achivement.getStatusText());

        achivementsSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment newComment = new Comment(MainActivity.user.getId(), achivementInputField.getText().toString());
                achivement.addComment(newComment);
                commentAdapter.Add(newComment);
                CompletableFuture.supplyAsync(() ->
                                MainActivity.serverApi.editUser(owner), executor)
                        .thenAccept(_user -> {
                            MainActivity.user = _user;
                        });
            }
        });
    }

    private void visibleForFinishedAchivement() {
        inputComment.setVisibility(View.VISIBLE);
        selector.setVisibility(View.GONE);
    }

    private void visibleForActiveAchivement() {
        inputComment.setVisibility(View.GONE);
        selector.setVisibility(View.VISIBLE);
        achivementImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewAchivement(Status.FAILED);
                Navigation.findNavController(view).navigate(R.id.action_achivementFragment_to_navigation_home);
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImageUri != null){
                    try {
                        new Thread(() -> {
//                                    MainActivity.user = MainActivity.serverApi.editUser(MainActivity.user);
//                                    System.out.println("editAccountConfirm: " + MainActivity.user);
                            byte[] imageBytes;
                            try {
                                InputStream inputStream = MainActivity.mainActivity.getContentResolver().openInputStream(selectedImageUri);
                                imageBytes = HelpFunctions.getBytes(inputStream);
                                inputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if(imageBytes != null){
                                MainActivity.serverApi.loadImageAchivement(imageBytes, achivement);
                            }
                        }).start();

                        achivement.setImage(imageName);
                        getNewAchivement(Status.COMPLETED);
                        Navigation.findNavController(view).navigate(R.id.action_achivementFragment_to_navigation_home);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void loadAchivementImage() {
        CompletableFuture.supplyAsync(() ->
                        MainActivity.serverApi.getImageAchivement(achivement), executor)
                .thenAccept(_bytes -> {
//                        System.out.println("BYTES: " + (_bytes != null));
                    if(_bytes != null){
                        Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                        getActivity().runOnUiThread(() -> {
                            achivementImage.setImageBitmap(photoUri);
                        });
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                achivementImage.setImageURI(selectedImageUri);
            }
        }
    }

    public void getNewAchivement(Status statusLastAchivement){
        CompletableFuture.supplyAsync(() ->
                        MainActivity.serverApi.getNewAchivement(statusLastAchivement), executor)
                .thenAccept(_user -> {
                    MainActivity.user = _user;
                });
    }
}