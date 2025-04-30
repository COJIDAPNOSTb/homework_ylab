package org.example.app.backend.model.dto;

import lombok.Value;

@Value
public class AuthRequest {
    String username;
    String password;
}