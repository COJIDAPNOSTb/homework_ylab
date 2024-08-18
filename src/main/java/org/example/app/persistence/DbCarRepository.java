package org.example.app.persistence;

import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.example.app.aspects.Log;
import org.example.app.config.ConfigDb;
import org.example.app.model.Car;
import org.example.app.repository.CarRepository;

public class DbCarRepository implements CarRepository {
    private static final Logger LOGGER = Logger.getLogger(DbCarRepository.class.getName());
    private final ConfigDb configDb;
    /**
     * Конструктор
     * @param configDb конфигурация подключения к базе данных
 
     */
    public DbCarRepository(ConfigDb configDb) {
        this.configDb = configDb;
    }

    @Override
    public void save(Car car) {
        String sql = "INSERT INTO ylabhw.cars (brand, model, year, price, condition) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCondition());
    
            int affectedRows = statement.executeUpdate();
        
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setId(generatedKeys.getInt(1)); 
                    }
                }
            } else {
                LOGGER.log(Level.SEVERE, "Inserting car failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving car", e);
        }
    }
    

    @Override
    public Car findById(int carId) {
        String sql = "SELECT * FROM ylabhw.cars WHERE id = ?";

        try (Connection connection = configDb.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, carId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToCar(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding car by id", e);
        }
        return null;
    }
    @Log
    @Override
    public List<Car> findAll() {
        String sql = "SELECT * FROM ylabhw.cars";

        List<Car> cars = new ArrayList<>();
        try (Connection connection = configDb.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all cars", e);
        }
        return cars;
    }

    @Override
    public void update(Car car) {
        String sql = "UPDATE ylabhw.cars SET brand = ?, model = ?, year = ?, price = ?, condition = ? WHERE id = ?";

        try (Connection connection = configDb.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCondition());
            statement.setInt(6, car.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating car", e);
        }
    }
/**
 * Метод удаления
 */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ylabhw.cars WHERE id = ?";

        try (Connection connection = configDb.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting car", e);
        }
    }
/**
 * Вспомогательный метод
 * @param resultSet
 * @return
 */
    private Car mapRowToCar(ResultSet resultSet) {
        try {
            return new Car(
                resultSet.getInt("id"),
                resultSet.getString("brand"),
                resultSet.getString("model"),
                resultSet.getInt("year"),
                resultSet.getDouble("price"),
                resultSet.getString("condition")
            );
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapping row to car", e);
            return null;
        }
    }
}
