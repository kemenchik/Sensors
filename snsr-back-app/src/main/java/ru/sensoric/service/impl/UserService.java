package ru.sensoric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sensoric.exception.ResourceNotFoundException;
import ru.sensoric.model.User;
import ru.sensoric.payload.ApiResponse;
import ru.sensoric.repository.UserRepository;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }

    public ResponseEntity<ApiResponse> activate(UUID uuid) throws UserPrincipalNotFoundException {
        User userData = userRepository.findByActivationCode(uuid).orElseThrow(
                () -> new UserPrincipalNotFoundException("User by activation code not found")
        );
        userData.setActivatedUser(true);
        userData.setActivationCode(null);
        userRepository.save(userData);
        return new ResponseEntity<>(new ApiResponse(true, "Email has been confirmed."), HttpStatus.OK);
    }

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findUserById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user_id", userId));
    }
}
