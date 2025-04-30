package org.example.app.backend.model.dto;

import lombok.Value;
import org.example.app.backend.model.Role;


@Value
public class UserCreateDto {
    String username;
    String password;
    Role role;
}
