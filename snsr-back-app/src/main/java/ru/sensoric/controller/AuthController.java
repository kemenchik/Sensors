package ru.sensoric.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sensoric.exception.ResourceNotFoundException;
import ru.sensoric.model.User;
import ru.sensoric.payload.ApiResponse;
import ru.sensoric.payload.JwtAuthenticationResponse;
import ru.sensoric.payload.LoginRequest;
import ru.sensoric.payload.SignUpRequest;
import ru.sensoric.security.JwtTokenProvider;
import ru.sensoric.service.impl.AuthenticationService;
import ru.sensoric.service.impl.UserService;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registration(signUpRequest);
    }

    @GetMapping("/current/{token}")
    public User getCurrentUser(@PathVariable String token) throws ResourceNotFoundException {
        return userService.getUserById(tokenProvider.getUserIdFromJWT(token));
    }

    @GetMapping("/activation/{uuid}")
    public ResponseEntity<ApiResponse> activation(@PathVariable UUID uuid) throws UserPrincipalNotFoundException {
        return userService.activate(uuid);
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestBody String token) {
        JSONObject tokenJson = new JSONObject(token);
        return tokenProvider.validateToken(tokenJson.get("value").toString());
    }
}
