
import org.example.app.model.Car;
import org.example.app.persistence.JdbcCarRepository;
import org.example.app.repository.CarRepository;
import org.example.app.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DisplayName("Тесты сервиса авто")
@Testcontainers
public class CarServiceTest {

    @Container
    public PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() throws Exception {
        String url = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        carRepository = new JdbcCarRepository(url, username, password);
        carService = new CarService(carRepository, null);

        // Создание таблицы и вставка данных
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE SCHEMA ylabhw");
                statement.execute("CREATE TABLE ylabhw.cars (id SERIAL PRIMARY KEY, brand VARCHAR(50), model VARCHAR(50), year INT, price DECIMAL, condition VARCHAR(20))");

                // Вставка данных
                statement.execute("INSERT INTO ylabhw.cars (brand, model, year, price, condition) VALUES ('BMW', 'X5', 2020, 60000, 'New')");
            }
        }
    }

    @Test
    public void testFindCarById() {
        Car car = carService.findCarById(1);
        assertEquals("BMW", car.getBrand());
        assertEquals("X5", car.getModel());
    }
    @Test
    public void testUpdateCar() {
        Car car = new Car("Toyota", "Camry", 2021, 30000, "Used");
        carRepository.save(car);

        car.setPrice(28000);
        car.setCondition("Like New");
        carRepository.update(car);

        Optional<Car> updatedCar = Optional.ofNullable(carRepository.findById(car.getId()));
        assertTrue(updatedCar.isPresent());
        assertEquals(28000.0, updatedCar.get().getPrice());
        assertEquals("Like New", updatedCar.get().getCondition());
    }


    @Test
    public void testFindCarByNonExistentId() {
        Optional<Car> foundCar = Optional.ofNullable(carRepository.findById(999));
        assertFalse(foundCar.isPresent());
    }   

}
