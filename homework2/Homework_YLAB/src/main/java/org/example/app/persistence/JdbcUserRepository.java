package org.example.app.persistence;


import org.example.app.config.ConfigDb;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcUserRepository implements UserRepository {

    private static final Logger LOGGER = Logger.getLogger(JdbcUserRepository.class.getName());
    private final ConfigDb configDb;
   

    public JdbcUserRepository(ConfigDb configDb) {
   
        this.configDb = configDb;
    }
  
    @Override
    public void save(User user) {
        String sql = "INSERT INTO ylabhw.users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());

            statement.executeUpdate();
        }catch(SQLException e)
        {
             LOGGER.log(Level.SEVERE, "Error saving user", e);
        }
    }

    @Override
    public User findByUsername(String username)  {
        String sql = "SELECT * FROM ylabhw.users WHERE username = ?";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        }catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Error finding user by username", e);
        }
        return null;
    }

    @Override
    public List<User> findAll()  {
        String sql = "SELECT * FROM ylabhw.users";

        try (Connection connection = configDb.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
            return users;
        }catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Error finding all users", e);
        }
        return null;
    }

    @Override
    public void update(User user)  {
        String sql = "UPDATE ylabhw.users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.getId());

            statement.executeUpdate();
        }catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ylabhw.users WHERE id = ?";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
             
        }catch(SQLException e)
        {  
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            
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
        
            LOGGER.log(Level.SEVERE, "Error mapping row to user", e);
        }
        return null;
    }

   @Override
    public User findById(int userId) {
        String sql = "SELECT * FROM ylabhw.users WHERE id = ?";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by id", e);
        }
        return null;
    }
}
