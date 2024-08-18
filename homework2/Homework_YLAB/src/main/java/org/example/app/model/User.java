package org.example.app.model;
/**Пользователь */
public class User {
    private static int counter = 0;
    private final int id;
    private String username;
    private String password;
    private Role role;
/**
 * конструктор
 * @param username логин
 * @param password пароль
 * @param role роль
 */
public User(int id, String username, String password, Role role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
}


public User(String username, String password, Role role) {
    this.id = ++counter;
    this.username = username;
    this.password = password;
    this.role = role;
}
/**Геттеры */
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
/**Сеттеры */
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
}
