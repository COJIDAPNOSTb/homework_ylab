package org.example.app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.app.aspects.Log;
import org.example.app.config.ConfigDb;
import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.User;
import org.example.app.repository.CarRepository;
import org.example.app.repository.OrderRepository;
import org.example.app.repository.UserRepository;

public class DbOrderRepository implements OrderRepository {
    private static final Logger LOGGER = Logger.getLogger(DbUserRepository.class.getName());
    private final ConfigDb configDb;

    private final UserRepository userRepository;
    private final CarRepository carRepository;

 public DbOrderRepository( UserRepository userRepository, CarRepository carRepository, ConfigDb configDb) {
       
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.configDb = configDb;
    }
    @Override
    public void save(Order order) {
        String sql = "INSERT INTO ylabhw.orders (customer_id, car_id, order_date, type, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
    
            statement.setInt(1, order.getCustomerId());
            statement.setInt(2, order.getCarId());
            if (order.getDate() != null) {
                statement.setTimestamp(3, Timestamp.valueOf(order.getDate().atStartOfDay()));
            } else {
                // Если date null, устанавливаем текущую дату
                statement.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            }
            statement.setString(4, order.getType());
            statement.setString(5, order.getStatus());
    int affectedRows = statement.executeUpdate();
    
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1)); 
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving order", e);
        }
    }
    

    @Override
    public Order findById(int orderId) {
        String sql = "SELECT * FROM ylabhw.orders WHERE id = ?";

        try (Connection connection = configDb.getConnection();
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
    @Log
    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM ylabhw.orders";

        List<Order> orders = new ArrayList<>();
        try (Connection connection = configDb.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error finding all orders", e);
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE ylabhw.orders SET customer_id = ?, car_id = ?, order_date = ?, type = ?, status = ? WHERE id = ?";

        try (Connection connection = configDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, order.getCustomerId());
            statement.setInt(2, order.getCarId());
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

        try (Connection connection = configDb.getConnection();
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
            
            Order order = new Order(
                resultSet.getInt("id"),
                customerId,
                carId,
                resultSet.getTimestamp("order_date").toLocalDateTime().toLocalDate(),
                resultSet.getString("type"),
                resultSet.getString("status")
            );
    
            // Отложенная загрузка объектов User и Car
            order.setCustomer(userRepository.findById(customerId));
            order.setCar(carRepository.findById(carId));
    
            return order;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapping row to order", e);
        }
        return null;
    }
    
    
}
