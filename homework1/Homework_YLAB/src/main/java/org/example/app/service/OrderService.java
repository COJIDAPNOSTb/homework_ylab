package org.example.app.service;

import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.User;
import org.example.app.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;




import java.time.LocalDate;

/**
 * Сервис отвечающий за логику управления заказами
 */
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
/**
 * Конструктор
 * @param orderRepository репозиторий заказов
 * @param userService сервис для управления пользователями
 */
    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }
/**
 * Метод отвечающий за создание нового заказа
 * @param user пользователь
 * @param car автмообиль
 * @param type тип заказа
 */
    public void createOrder(User user, Car car, String type) {
        List<Order> existingOrders = orderRepository.findAll().stream()
        .filter(order -> order.getCar().getId() == car.getId())
        .collect(Collectors.toList());

if (!existingOrders.isEmpty()) {
    System.out.println("Error: Order already exists for this car.");
    return;
}

        Order order = new Order(car, user, type);
        orderRepository.save(order);
        userService.logAction(user, "Created order: " + order.getId());
    }
/**
 * Метод отвечающий за обновление статуса заказа
 * @param user пользователь
 * @param orderId номер заказа
 * @param status статус
 */
    public void updateOrderStatus(User user, int orderId, String status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.update(order);
            userService.logAction(user, "Updated order status: " + order.getId());
        }
    }
/**
 * Метод отвкчающий за удаление заказа
 * @param user пользователь
 * @param orderId номер заказа
 */
    public void deleteOrder(User user, int orderId) {
        orderRepository.delete(orderId);
        userService.logAction(user, "Deleted order: " + orderId);
    }
/**
 * Метод отвечающий за лист заказов
 * @return лист всех заказов
 */
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }
/**
 * Метод отвечающий за поиск и фильтрацию списка заказов
 * @param date критерий
 * @param customer критерий
 * @param status критерий
 * @param carModel критерий
 * @return отфильрованный список
 */
    public List<Order> searchOrders(LocalDate date, String customer, String status, String carModel) {
        return orderRepository.findAll().stream()
                .filter(order -> (date == null || order.getDate().equals(date)) &&
                                 (customer == null || order.getCustomer().getUsername().equalsIgnoreCase(customer)) &&
                                 (status == null || order.getStatus().equalsIgnoreCase(status)) &&
                                 (carModel == null || order.getCar().getModel().equalsIgnoreCase(carModel))).collect(Collectors.toList());
    }
}
