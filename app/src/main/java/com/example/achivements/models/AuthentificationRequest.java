package com.example.achivements.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AuthentificationRequest {
    private String username, password;

    public AuthentificationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
