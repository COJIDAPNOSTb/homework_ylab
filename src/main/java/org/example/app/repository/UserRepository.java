package org.example.app.repository;

import org.example.app.model.User;
import java.util.List;
public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(int userId);
    User findById(int userId);
    User findByUsername(String username);
    List<User> findAll();
    
} 