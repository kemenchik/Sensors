package ru.sensoric.payload;

import lombok.Data;

@Data
public class SignUpRequest {

    private String username;

    private String password;

    private String email;

    public SignUpRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
