package com.example.achivements.ui.achivement;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchivementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchivementFragment extends Fragment {
    private Executor executor = Executors.newSingleThreadExecutor();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int SELECT_PICTURE = 1;
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
    private android.widget.ImageView achivementImage;
    private String imageName;
    private Uri selectedImageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achivement, container, false);

        Bundle args = getArguments();
        Achivement achivement = (Achivement) args.getSerializable("achivement");
        User owner = (User) args.getSerializable("owner");
        TextView achivementText = root.findViewById(R.id.achivement_text);
        TextView achivementLogin = root.findViewById(R.id.achivement_login);
        TextView achivementStatus = root.findViewById(R.id.achivement_status);
        TextView achivementInputField = root.findViewById(R.id.achivement_inputfield_comment);
        ImageView achivementsSender = root.findViewById(R.id.achivement_send_button);
        achivementImage = root.findViewById(R.id.achivement_image);
        RecyclerView achivementCommentRV = root.findViewById(R.id.achivement_comment_rv);
        Button cancelButton = root.findViewById(R.id.achivement_cancel_button);
        Button acceptButton = root.findViewById(R.id.achivement_accept_button);
        android.widget.LinearLayout inputComment = root.findViewById(R.id.achivement_input_comment);
        android.widget.LinearLayout selector = root.findViewById(R.id.achivement_selector);

        achivementCommentRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        CommentAdapter commentAdapter = new CommentAdapter();
        commentAdapter.Add((ArrayList<Comment>) achivement.getComments().stream().collect(Collectors.toList()));

        achivementCommentRV.setAdapter(commentAdapter);

        if(MainActivity.BottomNV != null) MainActivity.BottomNV.setVisibility(View.GONE);
        if(MainActivity.user == null) {
            inputComment.setVisibility(View.GONE);
            selector.setVisibility(View.GONE);
        }
        else{
            if(achivement.getImage() != null){
                String projectFolderPath = MainActivity.mainActivity.getApplicationContext().getFilesDir() + "/server/";
                File projectFolder = new File(projectFolderPath);
                if (!projectFolder.exists()) {
                    projectFolder.mkdir();
                }

                File avatarImageFile = new File(projectFolderPath + achivement.getImage());
                achivementImage.setImageURI(Uri.fromFile(avatarImageFile));
            }
            if(MainActivity.user.getAchivements().stream().anyMatch(_achivement -> _achivement.getId() == achivement.getId()) && achivement.getStatus() == Status.ACTIVE){
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
                                // Copy the selected image to the project folder
                                String projectFolderPath = MainActivity.mainActivity.getApplicationContext().getFilesDir() + "/server/";
                                File projectFolder = new File(projectFolderPath);
                                if (!projectFolder.exists()) {
                                    projectFolder.mkdir();
                                }

                                String imageName = new File("" + selectedImageUri).getName(); // Change the image name as needed
                                File avatarImageFile = new File(projectFolderPath + imageName);

                                InputStream inputStream = MainActivity.mainActivity.getContentResolver().openInputStream(selectedImageUri);
                                OutputStream outputStream = new FileOutputStream(avatarImageFile);

                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }

                                inputStream.close();
                                outputStream.close();

                                // Update user's avatarImage and set the ImageView
                                achivement.setImage(imageName);
                                getNewAchivement(Status.COMPLETED);
                                Navigation.findNavController(view).navigate(R.id.action_achivementFragment_to_navigation_home);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }else{
                inputComment.setVisibility(View.VISIBLE);
                selector.setVisibility(View.GONE);
                if(achivement.getImage() != null) {
                    String projectFolderPath = MainActivity.mainActivity.getApplicationContext().getFilesDir() + "/server/";
                    File avatarImageFile = new File(projectFolderPath + achivement.getImage());
                    if (avatarImageFile.exists())
                        achivementImage.setImageURI(Uri.fromFile(avatarImageFile));
                }
            }
        }
        achivementText.setText(achivement.getText());
        achivementLogin.setText(owner.getUsername());
        achivementStatus.setText("Achivement: " + achivement.getStatusText());

        achivementsSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment newComment = new Comment(MainActivity.user, achivementInputField.getText().toString());
                achivement.addComment(newComment);
                commentAdapter.Add(newComment);
//                CompletableFuture.supplyAsync(() ->
//                    MainActivity.serverApi.editUser(achivement.getUser()), executor)
//                        .thenAccept(_user -> {
//                            MainActivity.user = _user;
//                        });
            }
        });

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getNewAchivement(Status statusLastAchivement){
        CompletableFuture.supplyAsync(() ->
            MainActivity.serverApi.getNewAchivement(statusLastAchivement), executor)
                .thenAccept(_user -> {
                    MainActivity.user = _user;
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
//                Uri selectedImageUri = data.getData();
//                MainActivity.user.setAvatarImage(selectedImageUri);
//                avatarAccount.setImageURI(selectedImageUri);
                selectedImageUri = data.getData();
                achivementImage.setImageURI(selectedImageUri);
            }
        }
    }
}