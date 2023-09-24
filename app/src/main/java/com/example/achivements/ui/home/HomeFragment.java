package com.example.achivements.ui.home;

import static android.app.Activity.RESULT_OK;

import static com.example.achivements.MainActivity.CAMERA_PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.HelpFunctions;
import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.SettingsActivity;
import com.example.achivements.adapters.AchivementAdapter;
import com.example.achivements.adapters.FriendAdapter;
import com.example.achivements.databinding.FragmentHomeBinding;
import com.example.achivements.db.model.AchivementImage;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;
    private View root;
    private ImageView makeSelfieCamera;
    private static final int PICK_PICTURE_CAMERA = 3;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Achivement activeAchivement;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
//        MainActivity.mainActivity.getPermissions();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        if(MainActivity.user != null) {
            init();
            buttonListeners();
            putIntoTemplate();
            if(MainActivity.user.getFriends() != null){
                createFriendsAdapter(root);
            }
            if(MainActivity.user.getAchivements() != null){//Ачивки
                createAchivementsAdapter(root);
            }
        }

        return root;
    }

    private void putIntoTemplate() {
        loadAchivementImage();
    }

    private void loadAchivementImage() {
        new Thread(() -> {
            AchivementImage _achivementImageData = MainActivity.database.achivementImageDao().getImageByAchivementId(activeAchivement.getId());
            if(_achivementImageData != null) {
                Bitmap photoUri = BitmapFactory.decodeByteArray(_achivementImageData.imageData, 0, _achivementImageData.imageData.length);
                getActivity().runOnUiThread(() -> {
                    makeSelfieCamera.setImageBitmap(photoUri);
                });
            }
        }).start();
    }

    private void buttonListeners() {
        makeSelfieCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение на камеру уже предоставлено, открываем камеру
                    System.out.println("OPEN CAMERA");
                    openCamera();
                } else {
                    // Разрешение на камеру не предоставлено, запрашиваем его
                    System.out.println("PERMISSION CLOSED");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PICK_PICTURE_CAMERA);
    }

    public void init(){
        makeSelfieCamera = root.findViewById(R.id.make_selfie_field);
        activeAchivement = MainActivity.user.getActiveAchivement();
    }

    private void createAchivementsAdapter(View root) {
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

    private void createFriendsAdapter(View root) {
        RecyclerView friendsRV = root.findViewById(R.id.friends_rv);
        friendsRV.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        FriendAdapter friendAdapter = new FriendAdapter();
        friendAdapter.setActivity(getActivity());

        friendAdapter.Add(MainActivity.user.getFriends().stream()
                .collect(Collectors.toList()));

        friendsRV.setAdapter(friendAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout activeAchivement = view.findViewById(R.id.current_achivement);
        activeAchivement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HelpFunctions.achivement, (Serializable) MainActivity.user.getActiveAchivement());
                bundle.putSerializable(HelpFunctions.owner, (Serializable) MainActivity.user);
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_achivementFragment, bundle);
            }
        });
        if(MainActivity.BottomNV != null)
            MainActivity.BottomNV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PICTURE_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            makeSelfieCamera.setImageBitmap(imageBitmap);
            new Thread(() -> {
                AchivementImage _achivementImage = MainActivity.database.achivementImageDao().getImageByAchivementId(activeAchivement.getId());
                byte[] imageBytes;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageBytes = stream.toByteArray();

                if(_achivementImage == null){
                    _achivementImage = new AchivementImage();
                    _achivementImage.achivementId = activeAchivement.getId();
                    _achivementImage.imageData = imageBytes;
                    MainActivity.database.achivementImageDao().insert(_achivementImage);
                }else{
                    _achivementImage.imageData = imageBytes;
                    MainActivity.database.achivementImageDao().insert(_achivementImage);
                }
                getActivity().runOnUiThread(() -> {
                    Bundle args = new Bundle();
                    args.putBoolean("isSelfAccount", false);
                    args.putSerializable("owner", (Serializable) MainActivity.user);
                    args.putSerializable("achivement", (Serializable) activeAchivement);
                    Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_achivementFragment, args);
                });
            }).start();

            // Здесь вы можете преобразовать изображение в байтовый код
            // и вывести его в консоль.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }
}