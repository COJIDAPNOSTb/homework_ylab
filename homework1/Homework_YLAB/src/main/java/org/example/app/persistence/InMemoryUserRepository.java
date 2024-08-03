package org.example.app.persistence;

import org.example.app.model.User;
import org.example.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;



/**
 * Реализация интерфейса UserRepository
 */
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public void update(User user) {
        int index = findIndexById(user.getId());
        if (index != -1) {
            users.set(index, user);
        }
    }

    @Override
    public void delete(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    @Override
    public User findById(int userId) {
        return users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    private int findIndexById(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return i;
            }
        }
        return -1;
    }
}
