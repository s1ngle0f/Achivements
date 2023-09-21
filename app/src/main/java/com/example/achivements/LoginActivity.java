package com.example.achivements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.achivements.api.ServerApi;
import com.example.achivements.models.AuthentificationRequest;
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
                new Thread(() -> {
                    String jwt = null;
                    User _user;
                    if(loginReg == LoginReg.LOGIN) {
                        jwt = MainActivity.serverApi.login(new AuthentificationRequest(login.getText().toString(), password.getText().toString()));
                        System.out.println(login.getText().toString() + " " + password.getText().toString());
                        System.out.println("LoginReg.LOGIN: " + jwt);
                    }else if(loginReg == LoginReg.REG){
                        _user = MainActivity.serverApi.createUser(new User(login.getText().toString(), password.getText().toString()));
                        jwt = MainActivity.serverApi.login(new AuthentificationRequest(_user.getUsername(), _user.getPassword()));
                    }
                    if (jwt != null && !jwt.isEmpty()) {
                        System.out.println("jwt:" + jwt);
                        ServerApi.setJwt(jwt);
                        _user = MainActivity.serverApi.getUserByAuth();
                        if(_user != null){
                            MainActivity.editor.putString("jwt", jwt);
                            MainActivity.editor.apply();
                            MainActivity.user = _user;
                        }
                    }
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(myIntent);
                }).start();
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