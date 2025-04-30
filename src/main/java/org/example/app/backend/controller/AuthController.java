package org.example.app.backend.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.Role;
import org.example.app.backend.model.User;
import org.example.app.backend.model.dto.AuthRequest;
import org.example.app.backend.model.dto.UserCreateDto;
import org.example.app.backend.repository.UserRepository;
import org.example.app.backend.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Имя пользователя уже занято"));
        }


        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER);


        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "Пользователь успешно зарегистрирован"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Ошибка при регистрации: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);


            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new JwtAuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Неверное имя пользователя или пароль"));
        }
    }


    @Getter
    @AllArgsConstructor
    static class JwtAuthResponse {
        private String accessToken;
        private String tokenType = "Bearer";

        public JwtAuthResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }


    @Getter
    @AllArgsConstructor
    static class ApiResponse {
        private boolean success;
        private String message;
    }
}