package org.example.app.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.Order;
import org.example.app.backend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/admin-ui/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public PagedModel<Order> getAll(Pageable pageable) {
        Page<Order> orders = orderService.getAll(pageable);
        return new PagedModel<>(orders);
    }

    @GetMapping("/{id}")
    public Order getOne(@PathVariable UUID id) {
        return orderService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<Order> getMany(@RequestParam List<UUID> ids) {
        return orderService.getMany(ids);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @PatchMapping("/{id}")
    public Order patch(@PathVariable UUID id, @RequestBody JsonNode patchNode) throws IOException {
        return orderService.patch(id, patchNode);
    }

    @PatchMapping
    public List<UUID> patchMany(@RequestParam List<UUID> ids, @RequestBody JsonNode patchNode) throws IOException {
        return orderService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public Order delete(@PathVariable UUID id) {
        return orderService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<UUID> ids) {
        orderService.deleteMany(ids);
    }
}
