package ru.sensoric.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.sensoric.model.Role;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;

    private String password;

    private String email;

    private Role role;
}
