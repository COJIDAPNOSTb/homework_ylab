package org.example.app.persistence;


import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository {

    private final String url;
    private final String username;
    private final String password;

    public JdbcUserRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO ylabhw.users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());

            statement.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public User findByUsername(String username)  {
        String sql = "SELECT * FROM ylabhw.users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll()  {
        String sql = "SELECT * FROM ylabhw.users";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
            return users;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User user)  {
        String sql = "UPDATE ylabhw.users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.getId());

            statement.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ylabhw.users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
             
        }catch(SQLException e)
        {  
            e.printStackTrace();
            
        }
      
    }

    private User mapRowToUser(ResultSet resultSet) {
        try{return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );}
        catch(SQLException e)
        {
        
            e.printStackTrace();
        }
        return null;
    }

   @Override
    public User findById(int userId) {
        String sql = "SELECT * FROM ylabhw.users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
