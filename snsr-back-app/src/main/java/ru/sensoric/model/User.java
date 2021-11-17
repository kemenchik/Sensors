package ru.sensoric.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.sensoric.model.dto.UserDTO;
import ru.sensoric.payload.SignUpRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usrs")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column
    private Role role;

    @Column(name = "active")
    private boolean activatedUser;

    @Column
    private UUID activationCode;

    public User(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.password = encode(userDTO.getPassword());
        this.email = userDTO.getEmail();
        this.role = userDTO.getRole();
        this.activatedUser = true;
    }

    public User(SignUpRequest signUpRequest) {
        this.username = signUpRequest.getUsername();
        this.password = encode(signUpRequest.getPassword());
        this.email = signUpRequest.getEmail();
        this.role = Role.USER;
        this.activationCode = UUID.randomUUID();
        this.activatedUser = false;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(this.role.name())));
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return this.activatedUser;
    }

    private String encode(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }
}
