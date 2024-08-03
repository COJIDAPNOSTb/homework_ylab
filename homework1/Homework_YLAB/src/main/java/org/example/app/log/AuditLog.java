package org.example.app.log;

import java.util.Date;

import org.example.app.model.User;
/**Класс логирования */
public class AuditLog {
    private static int counter = 0;
    private final int id;
    private Date timestamp;
    private User user;
    private String action;
/**
 * Конструктор
 * @param user пользователь
 * @param action действие
 */
    public AuditLog(User user, String action) {
        this.id = ++counter;
        this.timestamp = new Date();
        this.user = user;
        this.action = action;
    }
/**Геттеры */
    public int getId() { return id; }
    public Date getTimestamp() { return timestamp; }
    public User getUser() { return user; }
    public String getAction() { return action; }
}
