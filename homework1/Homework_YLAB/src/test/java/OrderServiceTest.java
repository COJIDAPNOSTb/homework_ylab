
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.OrderRepository;
import org.example.app.service.*;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        userService = Mockito.mock(UserService.class);
        orderService = new OrderService(orderRepository, userService);
    }

    @Test
    public void testCreateOrderWithoutConflict() {
        User user = new User("customer", "password", Role.CUSTOMER);
        Car car = new Car("Toyota", "Camry", 2020, 30000.0, "New");
        List<Order> orders = new ArrayList<>();

        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        orderService.createOrder(user, car, "Purchase");

        Mockito.verify(orderRepository).save(Mockito.any(Order.class));
    }

    @Test
    public void testCreateOrderWithConflict() {
        User user = new User("customer", "password", Role.CUSTOMER);
        Car car = new Car("Toyota", "Camry", 2020, 30000.0, "New");
        Order existingOrder = new Order(car, user, "Purchase");
        List<Order> orders = new ArrayList<>();
        orders.add(existingOrder);

        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        orderService.createOrder(user, car, "Purchase");

        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }

    @Test
    public void testUpdateOrderStatus() {
        User user = new User("admin", "password", Role.ADMIN);
        Order order = new Order(new Car("Toyota", "Camry", 2020, 30000.0, "New"), user, "Purchase");
        Mockito.when(orderRepository.findById(order.getId())).thenReturn(order);

        orderService.updateOrderStatus(user, order.getId(), "Completed");

        Mockito.verify(orderRepository).update(Mockito.any(Order.class));
        assertThat(order.getStatus()).isEqualTo("Completed");
    }

    @Test
    public void testDeleteOrder() {
        User user = new User("admin", "password", Role.ADMIN);
        Order order = new Order(new Car("Toyota", "Camry", 2020, 30000.0, "New"), user, "Purchase");
        Mockito.when(orderRepository.findById(order.getId())).thenReturn(order);

        orderService.deleteOrder(user, order.getId());

        Mockito.verify(orderRepository).delete(order.getId());
    }

    @Test
    public void testListOrders() {
        List<Order> orders = List.of(
                new Order(new Car("Toyota", "Camry", 2020, 30000.0, "New"), new User("customer1", "password", Role.CUSTOMER), "Purchase"),
                new Order(new Car("Honda", "Civic", 2019, 20000.0, "Used"), new User("customer2", "password", Role.CUSTOMER), "Service")
        );
        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.listOrders();

        assertThat(result).isEqualTo(orders);
    }

    @Test
    public void testSearchOrders() {
        List<Order> orders = List.of(
                new Order(new Car("Toyota", "Camry", 2020, 30000.0, "New"), new User("customer1", "password", Role.CUSTOMER), "Purchase"),
                new Order(new Car("Honda", "Civic", 2019, 20000.0, "Used"), new User("customer2", "password", Role.CUSTOMER), "Service")
        );
        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.searchOrders(null, "customer1", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomer().getUsername()).isEqualTo("customer1");
    }
}
