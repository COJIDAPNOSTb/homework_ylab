package org.example.app.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.Order;
import org.example.app.backend.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOne(UUID id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<Order> getMany(List<UUID> ids) {
        return orderRepository.findAllById(ids);
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Order patch(UUID id, JsonNode patchNode) throws IOException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(order).readValue(patchNode);

        return orderRepository.save(order);
    }

    public List<UUID> patchMany(List<UUID> ids, JsonNode patchNode) throws IOException {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            objectMapper.readerForUpdating(order).readValue(patchNode);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream()
                .map(Order::getId)
                .toList();
    }

    public Order delete(UUID id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return order;
    }

    public void deleteMany(List<UUID> ids) {
        orderRepository.deleteAllById(ids);
    }
}
