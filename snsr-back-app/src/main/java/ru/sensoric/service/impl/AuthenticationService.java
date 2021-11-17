package ru.sensoric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sensoric.exception.AppException;
import ru.sensoric.model.User;
import ru.sensoric.payload.ApiResponse;
import ru.sensoric.payload.JwtAuthenticationResponse;
import ru.sensoric.payload.LoginRequest;
import ru.sensoric.payload.SignUpRequest;
import ru.sensoric.repository.UserRepository;
import ru.sensoric.security.JwtTokenProvider;

import java.net.URI;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSenderService senderService;

    @Value("${server.real-ip}")
    private String realIp;

    public ResponseEntity authenticate(LoginRequest loginRequest) {

        User candidate = userRepository.findUserByUsername(loginRequest.getUsernameOrEmail()).orElse(
                userRepository.findUserByEmail(loginRequest.getUsernameOrEmail()).orElse(null)
        );

        if (candidate != null) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            candidate.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, principal));
        }
        return ResponseEntity.badRequest().body("No user with such credentials.");
    }

    public ResponseEntity<ApiResponse> registration(SignUpRequest signUpRequest) {
        if (userRepository.existsUserByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.OK);
        }

        if (userRepository.existsUserByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.OK);
        }

        User user = new User(signUpRequest);

        try {
            senderService.send(user.getEmail(), "Проект Сенсорика", "Ссылка для активации: https://" + realIp + "/api/auth/activation/" + user.getActivationCode());
        } catch (Exception e) {
            throw new AppException("Some problems with sending verification email. Check your email.");
        }

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
