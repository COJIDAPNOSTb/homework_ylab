package org.example.app.backend;

import org.example.app.backend.config.SecurityConfig;
import org.example.app.backend.model.Role;
import org.example.app.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class HomeworkFullApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeworkFullApplication.class, args);
       
    }



}

