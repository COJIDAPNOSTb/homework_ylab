package org.example.app.service;

import org.example.app.dto.OrderDTO;
import org.example.app.mapper.OrderMapper;
import org.example.app.model.Order;
import org.example.app.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(int id) {
        Order order = orderRepository.findById(id);
        return order != null ? orderMapper.toDTO(order) : null;
    }

    public void saveOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);
    }

    public void updateOrder(int id, OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        order.setId(id);
        orderRepository.update(order);
    }

    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }
}
