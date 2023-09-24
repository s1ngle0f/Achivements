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
    EditText descriptionField;
    TextView editSettLogin;
    Button editAccountConfirm;
    Button editAccountExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        avatarAccount = findViewById(R.id.edit_account_image);
        descriptionField = findViewById(R.id.edit_account_descripton);
        editSettLogin = findViewById(R.id.edit_account_login);
        editAccountConfirm = findViewById(R.id.edit_account_confirm);
        editAccountExit= findViewById(R.id.edit_account_exit);

        setUserData();

        editAccountConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.user.setDescription(descriptionField.getText().toString());
                new Thread(() -> {
                    MainActivity.user = MainActivity.serverApi.editUser(MainActivity.user);
                    if(imagePath != null){
                        MainActivity.serverApi.loadAvatar("avatar.png", imageBytes);
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

                imagePath = selectedImageUri.getPath();
                avatarImageFile = new File(imagePath);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    imageBytes = HelpFunctions.getBytes(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap photoUri = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                avatarAccount.setImageBitmap(photoUri);
            }
        }
    }

    private void setUserData(){
        if(MainActivity.user != null){
            descriptionField.setText(MainActivity.user.getDescription());
            editSettLogin.setText(MainActivity.user.getUsername());
        }

        CompletableFuture.supplyAsync(() ->
                        MainActivity.serverApi.getAvatar(), executor)
                .thenAccept(_bytes -> {
                    if(_bytes != null){
                        Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                        avatarAccount.setImageBitmap(photoUri);
                    }
                });
    }
}