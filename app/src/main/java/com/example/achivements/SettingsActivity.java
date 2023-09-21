package com.example.achivements;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.achivements.api.ServerApi;
import com.example.achivements.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import carbon.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {
    private Executor executor = Executors.newSingleThreadExecutor();
    private ImageView avatarAccount;
    private byte[] imageBytes;
    private String imagePath;
    private File avatarImageFile;
    private static int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        avatarAccount = findViewById(R.id.edit_account_image);
        EditText descriptionField = findViewById(R.id.edit_account_descripton);
        TextView editSettLogin = findViewById(R.id.edit_account_login);
        Button editAccountConfirm = findViewById(R.id.edit_account_confirm);
        Button editAccountExit= findViewById(R.id.edit_account_exit);

        descriptionField.setText(MainActivity.user.getDescription());
        editSettLogin.setText(MainActivity.user.getUsername());

        CompletableFuture.supplyAsync(() ->
                        MainActivity.serverApi.getAvatar(), executor)
                .thenAccept(_bytes -> {
                    if(_bytes != null){
                        Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                        avatarAccount.setImageBitmap(photoUri);
                    }
                });


//        String projectFolderPath = getApplicationContext().getFilesDir() + "/project/";
//        String imageName = "avatar.jpg";
//        File avatarImageFile = new File(projectFolderPath + imageName);
//        if(avatarImageFile.exists())
//            avatarAccount.setImageURI(Uri.fromFile(avatarImageFile));

        editAccountConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.user.setDescription(descriptionField.getText().toString());
                new Thread(() -> {
                    MainActivity.user = MainActivity.serverApi.editUser(MainActivity.user);
                    if(avatarImageFile != null){
                        MainActivity.serverApi.loadAvatar(avatarImageFile);
                    }
                }).start();
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        editAccountExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.editor.clear();
                MainActivity.editor.apply();
                MainActivity.user = null;
                ServerApi.setJwt("");
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        avatarAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                // Copy the selected image to the project folder
                String projectFolderPath = getApplicationContext().getFilesDir() + "/project/";
                File projectFolder = new File(projectFolderPath);
                if (!projectFolder.exists()) {
                    projectFolder.mkdir();
                }

                String imageName = "avatar.jpg";
                avatarImageFile = new File(projectFolderPath + imageName);

                CompletableFuture.runAsync(() ->
                        MainActivity.serverApi.loadAvatar(selectedImageUri.getPath()), executor);
                Uri uriFromFile = Uri.fromFile(avatarImageFile);
                avatarAccount.setImageURI(uriFromFile);
                imageBytes = HelpFunctions.getBytesFromUri(uriFromFile, getContentResolver());
            }
        }
    }
}