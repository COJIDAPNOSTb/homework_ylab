package org.example.app.persistence;


import org.example.app.model.Order;
import org.example.app.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * Реализация интерфейса OrderRepository
 */
public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public void update(Order order) {
        int index = findIndexById(order.getId());
        if (index != -1) {
            orders.set(index, order);
        }
    }

    @Override
    public void delete(int orderId) {
        orders.removeIf(order -> order.getId() == orderId);
    }

    @Override
    public Order findById(int orderId) {
        return orders.stream().filter(order -> order.getId() == orderId).findFirst().orElse(null);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    private int findIndexById(int orderId) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == orderId) {
                return i;
            }
        }
        return -1;
    }
}
