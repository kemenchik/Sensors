package ru.sensoric.payload;

import lombok.Data;
import ru.sensoric.model.User;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private final String tokenType = "Bearer";
    private User principal;

    public JwtAuthenticationResponse(String accessToken, User principal) {
        this.accessToken = accessToken;
        this.principal = principal;
    }
}
