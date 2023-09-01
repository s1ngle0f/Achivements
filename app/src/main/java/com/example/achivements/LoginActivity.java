package com.example.achivements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.achivements.models.User;

public class LoginActivity extends AppCompatActivity {
    enum LoginReg{
        LOGIN,
        REG
    }
    static LoginReg loginReg = LoginReg.LOGIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EditText login = findViewById(R.id.login_login);
        EditText password = findViewById(R.id.login_password);
        EditText passwordTwice = findViewById(R.id.login_password_twice);
        Button sumbit = findViewById(R.id.login_button);
        Button switcher = findViewById(R.id.login_to_reg);

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accessToken = null;
                if(loginReg == LoginReg.LOGIN) {
                    accessToken = MainActivity.ServerEmulator.GetAccessCode(login.getText().toString(), password.getText().toString());
                }else if(loginReg == LoginReg.REG){
                    accessToken = MainActivity.ServerEmulator.CreateUser(login.getText().toString(), password.getText().toString());
                    System.out.println(accessToken);
                }
                if (accessToken != null) {
                    User newUser = MainActivity.ServerEmulator.GetUserInfo(login.getText().toString(), accessToken);
                    if(newUser != null){
                        MainActivity.editor.putString("login", login.getText().toString());
                        MainActivity.editor.putString("accessToken", accessToken);
                        MainActivity.editor.apply();
                        newUser.setAccessToken(accessToken);
                        System.out.println("newUser " + newUser);
                        MainActivity.user = newUser;
                    }
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//                    myIntent.putExtra("user", newUser);
                    startActivity(myIntent);
                }
            }
        });

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginReg == LoginReg.LOGIN){
                    sumbit.setText("Зарегистрироваться");
                    sumbit.setTextSize(11);
                    switcher.setText("Вход");
                    passwordTwice.setVisibility(View.VISIBLE);
                    loginReg = LoginReg.REG;
                } else if(loginReg == LoginReg.REG){
                    sumbit.setText("Войти");
                    sumbit.setTextSize(16);
                    switcher.setText("Регистрация");
                    passwordTwice.setVisibility(View.GONE);
                    loginReg = LoginReg.LOGIN;
                }
            }
        });
    }
}