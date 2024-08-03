package org.example.app.repository;

import java.util.List;

import org.example.app.model.User;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(int userId);
    User findById(int userId);
    User findByUsername(String username);
    List<User> findAll();
}
