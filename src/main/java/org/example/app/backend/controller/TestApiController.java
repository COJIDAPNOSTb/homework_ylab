package org.example.app.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestApiController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.ok("Это публичный API endpoint");
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> authenticatedEndpoint() {
        return ResponseEntity.ok("Вы успешно аутентифицированы через JWT");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userEndpoint() {
        return ResponseEntity.ok("У вас роль USER");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminEndpoint() {
        return ResponseEntity.ok("У вас роль ADMIN");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не аутентифицирован");
        }

        return ResponseEntity.ok("Текущий пользователь: " + authentication.getName());
    }
}