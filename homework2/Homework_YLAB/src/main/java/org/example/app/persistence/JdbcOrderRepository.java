package org.example.app.persistence;

import org.example.app.model.*;
import org.example.app.repository.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcOrderRepository implements OrderRepository {

    private static final Logger LOGGER = Logger.getLogger(JdbcOrderRepository.class.getName());

    private final String url;
    private final String username;
    private final String password;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public JdbcOrderRepository(String url, String username, String password, UserRepository userRepository, CarRepository carRepository) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO ylabhw.orders (customer_id, car_id, order_date, type, status) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            statement.setInt(1, order.getCustomer().getId());
            statement.setInt(2, order.getCar().getId());
            // Преобразование LocalDate в Timestamp
            statement.setTimestamp(3, Timestamp.valueOf(order.getDate().atStartOfDay())); 
            statement.setString(4, order.getType());
            statement.setString(5, order.getStatus());
    
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving order", e);
        }
    }
    

    @Override
    public Order findById(int orderId) {
        String sql = "SELECT * FROM ylabhw.orders WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToOrder(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding order by id", e);
        }
        return null;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM ylabhw.orders";

        List<Order> orders = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all orders", e);
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE ylabhw.orders SET customer_id = ?, car_id = ?, order_date = ?, type = ?, status = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, order.getCustomer().getId());
            statement.setInt(2, order.getCar().getId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getDate().atStartOfDay())); 
            statement.setString(4, order.getType());
            statement.setString(5, order.getStatus());
            statement.setInt(6, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ylabhw.orders WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting order", e);
        }
    }

    private Order mapRowToOrder(ResultSet resultSet) {
        try {
            int customerId = resultSet.getInt("customer_id");
            int carId = resultSet.getInt("car_id");
    
            User customer = userRepository.findById(customerId);
            Car car = carRepository.findById(carId);
    
            // Проверка на null
            if (customer != null && car != null) {
                return new Order(
                    resultSet.getInt("id"),
                    customer,
                    car,
                    resultSet.getTimestamp("order_date").toLocalDateTime().toLocalDate(),
                    resultSet.getString("type"),
                    resultSet.getString("status")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapping row to order", e);
        }
        return null;
    }
    
}
