package org.example.app.repository;


import java.util.List;

import org.example.app.model.Order;

public interface OrderRepository {
    void save(Order order);
    void update(Order order);
    void delete(int orderId);
    Order findById(int orderId);
    List<Order> findAll();
}
