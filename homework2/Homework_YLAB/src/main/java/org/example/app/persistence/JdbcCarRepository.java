package org.example.app.persistence;



import org.example.app.model.Car;
import org.example.app.repository.CarRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**Реализация репозитория с помощью базы данных */
public class JdbcCarRepository implements CarRepository {

    private static final Logger LOGGER = Logger.getLogger(JdbcCarRepository.class.getName());

    private final String url;
    private final String username;
    private final String password;
    /**
     * Конструктор
     * @param url ссылка
     * @param username логин
     * @param password пароль
     */
    public JdbcCarRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void save(Car car) {
        String sql = "INSERT INTO ylabhw.cars (brand, model, year, price, condition) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCondition());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving car", e);
        }
    }

    @Override
    public Car findById(int carId) {
        String sql = "SELECT * FROM ylabhw.cars WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
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

    @Override
    public List<Car> findAll() {
        String sql = "SELECT * FROM ylabhw.cars";

        List<Car> cars = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
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

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
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

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
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
