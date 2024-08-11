
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.persistence.JdbcUserRepository;
import org.example.app.repository.UserRepository;




import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@DisplayName("Тесты пользовательского сервиса")
public class UserServiceTest {

    @Container
    public PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        String url = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

     
        Connection connection = DriverManager.getConnection(url, username, password);

    
        userRepository = new JdbcUserRepository(url, username, password);


        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA ylabhw");
            statement.execute("CREATE TABLE ylabhw.users (id SERIAL PRIMARY KEY, username VARCHAR(50), password VARCHAR(100), role VARCHAR(20))");
            statement.execute("INSERT INTO ylabhw.users (username, password, role) VALUES ('testUser', 'testPassword', 'CUSTOMER')");
        }
    }

    @Test
    @DisplayName("Поиск пользователя")
    public void testFindByUsername() {
        User user = userRepository.findByUsername("testUser");
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals(Role.CUSTOMER, user.getRole());
    }
    @Test
    public void testSaveAndFindUserByUsername() {
        User user = new User("testUser", "password", Role.CUSTOMER);
        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("password", foundUser.getPassword());
    }
    @Test
    public void testUpdateUser() {
        User user = new User("updateUser", "initialPassword", Role.ADMIN);
        userRepository.save(user);

        user.setPassword("newPassword");
        userRepository.update(user);

        Optional<User> updatedUser = Optional.ofNullable(userRepository.findById(user.getId()));
        assertNotNull(updatedUser);
        assertEquals("newPassword", updatedUser.get().getPassword());
    }
    @Test
    public void testFindUserByNonExistentUsername() {
        User foundUser = userRepository.findByUsername("nonExistentUser");
        assertNull(foundUser);
    }



    @AfterEach
    public void tearDown() throws Exception {
        postgresContainer.stop();
    }
}
