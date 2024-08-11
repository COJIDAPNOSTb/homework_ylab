package org.example.app.service;

import org.example.app.log.AuditLog;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.UserRepository;

import java.util.List;

import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UserService {
    private final UserRepository userRepository;
    private final List<AuditLog> auditLogs = new ArrayList<>();
/**
 * Конструктор
 * @param userRepository пользовательский репозиторий
 */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
/**
 * Метод отвечающий за регистрацию
 * @param username логин
 * @param password пароль
 * @param role роль
 */
    public void register(String username, String password, Role role) {
        User user = new User(username, password, role);
        userRepository.save(user);
        logAction(user, "---"+role+"---Registered");
    }
/**
 * Метод отвечающий за авторизацию
 * @param username логин
 * @param password пароль
 * @return в случае успешной проверки возвращает пользователя
 */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            logAction(user, "Logged in");
            return user;
        }
        return null;
    }
/**
 * Метод отвечающий за список пользвотелей
 * @return список всех пользователей
 */
    public List<User> listUsers() {
        return userRepository.findAll();
    }
    /**
     * Метод отвечающий за поиск пользователя по его логину
     * @param username логин
     * @return пользователя по логину
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
/**
 * Метод отвечающий за обновление пользователя
 * @param userId id пользователя
 * @param username логин 
 * @param password пароль
 * @param role роль
 */
    public void updateUser(int userId, String username, String password, Role role) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            userRepository.update(user);
            logAction(user, "Updated user details");
        }
    }
/**
 * Метод отвечающий за удаление пользователя
 * @param userId id пользователя
 */
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            userRepository.delete(userId);
            logAction(user, "Deleted user");
        }
    }
/**
 * Метод отвечающий за логи
 * @return лист лог
 */
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }
/**
 * Метод отвечающий за сохранение лога
 * @param user пользователь
 * @param action действие
 */
    public void logAction(User user, String action) {
        auditLogs.add(new AuditLog(user, action));
    }
/**
 * Метод отвечающий за экспорт логов в виде текстового файла
 */
    public void exportAuditLogs() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("audit_logs.txt"))) {
            for (AuditLog log : auditLogs) {
                writer.write(log.getTimestamp() + " - " + log.getUser().getUsername() + " - " + log.getAction());
                writer.newLine();
            }
            System.out.println("Successfully!");
        } catch (IOException e) {
            System.out.println("ERROR while exporting audit logs: " + e.getMessage());
        }
    }
}
