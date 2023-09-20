package com.example.achivements.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthentificationRequest {
    private String username, password;
}
