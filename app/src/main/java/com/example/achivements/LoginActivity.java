package com.example.achivements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.achivements.api.ServerApi;
import com.example.achivements.models.AuthentificationRequest;
import com.example.achivements.models.User;

public class LoginActivity extends AppCompatActivity {
    enum LoginReg{
        LOGIN,
        REG
    }
    static LoginReg loginReg = LoginReg.LOGIN;
    private static final String register = "Зарегистрироваться";
    private static final String registration = "Регистрация";
    private static final String entrance = "Вход";
    private static final String enter = "Войти";
    private static final String INVALID_DATA = "Введеные данные неправильные";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.serverApi == null)
            MainActivity.serverApi = new ServerApi();

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
                    logIn(
                            login.getText().toString(),
                            password.getText().toString(),
                            passwordTwice.getText().toString());
                }).start();
            }
        });

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginReg == LoginReg.LOGIN){
                    sumbit.setText(register);
                    sumbit.setTextSize(11);
                    switcher.setText(entrance);
                    passwordTwice.setVisibility(View.VISIBLE);
                    loginReg = LoginReg.REG;
                } else if(loginReg == LoginReg.REG){
                    sumbit.setText(enter);
                    sumbit.setTextSize(16);
                    switcher.setText(registration);
                    passwordTwice.setVisibility(View.GONE);
                    loginReg = LoginReg.LOGIN;
                }
            }
        });
    }

    private void logIn(String login, String password, String passwordTwice){
        String jwt = null;
        User _user = null;
        if(loginReg == LoginReg.LOGIN) {
            jwt = MainActivity.serverApi.login(new AuthentificationRequest(login, password));
        }else if(loginReg == LoginReg.REG && password.equals(passwordTwice)){
            _user = MainActivity.serverApi.createUser(new User(login, password));
            if(_user != null)
                jwt = MainActivity.serverApi.login(new AuthentificationRequest(_user.getUsername(), password));
        }
        System.out.println(_user);
        System.out.println(jwt);
        if (jwt != null && !jwt.isEmpty()) {
            ServerApi.setJwt(jwt);
            _user = MainActivity.serverApi.getUserByAuth();
            if(_user != null){
                MainActivity.editor.putString(HelpFunctions.jwt, jwt);
                MainActivity.editor.apply();
                MainActivity.user = _user;
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(myIntent);
            }else{
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), INVALID_DATA, Toast.LENGTH_SHORT).show();
                });
            }
        }else{
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(), INVALID_DATA, Toast.LENGTH_SHORT).show();
            });
        }
    }
}