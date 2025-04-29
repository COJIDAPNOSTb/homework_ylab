package org.example.app.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.Car;
import org.example.app.backend.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/admin-ui/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public PagedModel<Car> getAll(Pageable pageable) {
        Page<Car> cars = carService.getAll(pageable);
        return new PagedModel<>(cars);
    }

    @GetMapping("/{id}")
    public Car getOne(@PathVariable UUID id) {
        return carService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<Car> getMany(@RequestParam List<UUID> ids) {
        return carService.getMany(ids);
    }

    @PostMapping
    public Car create(@RequestBody Car car) {
        return carService.create(car);
    }

    @PatchMapping("/{id}")
    public Car patch(@PathVariable UUID id, @RequestBody JsonNode patchNode) throws IOException {
        return carService.patch(id, patchNode);
    }

    @PatchMapping
    public List<UUID> patchMany(@RequestParam List<UUID> ids, @RequestBody JsonNode patchNode) throws IOException {
        return carService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public Car delete(@PathVariable UUID id) {
        return carService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<UUID> ids) {
        carService.deleteMany(ids);
    }
}
