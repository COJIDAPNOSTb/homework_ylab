
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.Statement;
import org.example.app.service.*;
import org.example.app.repository.*;
import org.example.app.config.ConfigDb;
import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.User;
import org.example.app.persistence.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("Положительные и отрицательные тесты сервиса заказов")
@Testcontainers
public class OrderServiceTest extends ContainerTest {

    private OrderService orderService;
    private UserService userService;
    private CarService carService;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private OrderRepository orderRepository;
    @BeforeEach
    public void setUp() throws Exception {
        String url = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        ConfigDb config = new ConfigDb(url,username,password);
        
        userRepository = new JdbcUserRepository(config);
        carRepository = new JdbcCarRepository(config);
        orderRepository = new JdbcOrderRepository(userRepository, carRepository,config);

        userService = new UserService(userRepository);
        carService = new CarService(carRepository,userService);
        orderService = new OrderService(carRepository, orderRepository,userService);
        // Создание схемы и таблиц
        try (Connection connection = config.getConnection();) {
            try (Statement statement = connection.createStatement()) {
              
                statement.execute("CREATE SCHEMA IF NOT EXISTS ylabhw");
    
        
                statement.execute("CREATE TABLE IF NOT EXISTS ylabhw.users (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "username VARCHAR(50), " +
                                  "password VARCHAR(100), " +
                                  "role VARCHAR(20))");
    
            
                statement.execute("INSERT INTO ylabhw.users (username, password, role) " +
                                  "VALUES ('testUser', 'testPassword', 'CUSTOMER')");
    
           
                statement.execute("CREATE TABLE IF NOT EXISTS ylabhw.cars (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "brand VARCHAR(50), " +
                                  "model VARCHAR(50), " +
                                  "year INT, " +
                                  "price DECIMAL, " +
                                  "condition VARCHAR(20))");
    
           
                statement.execute("INSERT INTO ylabhw.cars (brand, model, year, price, condition) " +
                                  "VALUES ('BMW', 'X5', 2020, 60000, 'New')");
    
          
                statement.execute("CREATE TABLE IF NOT EXISTS ylabhw.orders (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "customer_id INT REFERENCES ylabhw.users(id), " +
                                  "car_id INT REFERENCES ylabhw.cars(id), " +
                                  "order_date DATE, " +
                                  "type VARCHAR(50), " +
                                  "status VARCHAR(50))");
    
         
                statement.execute("INSERT INTO ylabhw.orders (customer_id, car_id, order_date, type, status) " +
                                  "VALUES (1, 1, '2024-08-10', 'Purchase', 'Pending')");
            }
        }
    }
    

    @Test
    public void testFindOrderById() {
        Order order = orderService.findOrderById(1);
        assertEquals("Pending", order.getStatus());
        assertEquals("Purchase", order.getType());
    }
    @Test
    public void testCreateOrder() {
        User user = userService.login("testUser", "testPassword");
        Car car = carService.findCarById(1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(user, car, "Service");
        });
    
        assertEquals("Order for this car already exists.", exception.getMessage());
    }
    

    @Test
    public void testUpdateOrderStatus() {
        User user = userService.login("testUser", "testPassword");
        orderService.updateOrderStatus(user, 1 ,"Completed");
        Order order = orderService.findOrderById(1);
        assertEquals("Completed", order.getStatus());
    }

    @Test
    public void testFindOrderByIdNotFound() {
        Order order = orderService.findOrderById(999);
        assertNull(order);
    }



  @Test
public void testUpdateOrderStatusInvalidOrder() {
    User user = userService.login("testUser", "testPassword");


    assertDoesNotThrow(() -> {
        orderService.updateOrderStatus(user, 999, "Completed");
    });
}

}