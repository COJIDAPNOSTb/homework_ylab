package org.example.app.repository;

import org.example.app.model.Order;
import java.util.List;
public interface OrderRepository {
    void save(Order order);
    void update(Order order);
    void delete(int orderId);
    Order findById(int orderId);
    List<Order> findAll();
}
