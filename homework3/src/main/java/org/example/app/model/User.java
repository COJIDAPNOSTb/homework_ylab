package org.example.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private int id;
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
@JsonCreator
public User(@JsonProperty("username")String username, @JsonProperty("password") String password,  @JsonProperty("role") Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
}
  public User()
  {
    
  }
  
/**Геттеры */
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
/**Сеттеры */
    public void setId(int id){this.id = id;}
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
}

