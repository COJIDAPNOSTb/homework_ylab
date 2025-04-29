package org.example.app.backend.model.dto;

import lombok.Value;
import org.example.app.backend.model.Role;
import org.example.app.backend.model.User;

import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto {
    UUID id;
    String username;
    Role role;
}