
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.example.app.log.AuditLog;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.UserRepository;
import org.example.app.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;



public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testRegisterCustomer() {
        User user = new User("testUser", "testPassword", Role.CUSTOMER);

        userService.register(user.getUsername(), user.getPassword(), user.getRole());

        Mockito.verify(userRepository).save(Mockito.any(User.class));
        List<AuditLog> logs = userService.getAuditLogs();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getAction()).isEqualTo("---CUSTOMER---Registered");
    }
    @Test
    public void testRegisterManager() {
        User user = new User("testUser", "testPassword", Role.MANAGER);

        userService.register(user.getUsername(), user.getPassword(), user.getRole());

        Mockito.verify(userRepository).save(Mockito.any(User.class));
        List<AuditLog> logs = userService.getAuditLogs();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getAction()).isEqualTo("---MANAGER---Registered");
    }
    @Test
    public void testRegisterAdmin() {
        User user = new User("testUser", "testPassword123", Role.ADMIN);

        userService.register(user.getUsername(), user.getPassword(), user.getRole());

        Mockito.verify(userRepository).save(Mockito.any(User.class));
        List<AuditLog> logs = userService.getAuditLogs();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getAction()).isEqualTo("---ADMIN---Registered");
    }
    @Test
    public void testLogin() {
        User user = new User("testUser", "testPassword", Role.CUSTOMER);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        User loggedInUser = userService.login(user.getUsername(), user.getPassword());

        assertThat(loggedInUser).isEqualTo(user);
        List<AuditLog> logs = userService.getAuditLogs();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getAction()).isEqualTo("Logged in");
    }


    @Test
    public void testUpdateUser() {
        User user = new User("testUser", "testPassword", Role.CUSTOMER);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(user);

        userService.updateUser(user.getId(), "updatedUser", "updatedPassword", Role.MANAGER);

        Mockito.verify(userRepository).update(Mockito.any(User.class));
        assertThat(user.getUsername()).isEqualTo("updatedUser");
        assertThat(user.getPassword()).isEqualTo("updatedPassword");
        assertThat(user.getRole()).isEqualTo(Role.MANAGER);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser", "testPassword", Role.CUSTOMER);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(user);

        userService.deleteUser(user.getId());

        Mockito.verify(userRepository).delete(user.getId());
        List<AuditLog> logs = userService.getAuditLogs();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getAction()).isEqualTo("Deleted user");
    }
}
