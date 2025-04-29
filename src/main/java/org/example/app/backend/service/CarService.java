package org.example.app.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.Car;
import org.example.app.backend.repository.CarRepository;
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
public class CarService {

    private final CarRepository carRepository;

    private final ObjectMapper objectMapper;

    public Page<Car> getAll(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car getOne(UUID id) {
        Optional<Car> carOptional = carRepository.findById(id);
        return carOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<Car> getMany(List<UUID> ids) {
        return carRepository.findAllById(ids);
    }

    public Car create(Car car) {
        return carRepository.save(car);
    }

    public Car patch(UUID id, JsonNode patchNode) throws IOException {
        Car car = carRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(car).readValue(patchNode);

        return carRepository.save(car);
    }

    public List<UUID> patchMany(List<UUID> ids, JsonNode patchNode) throws IOException {
        Collection<Car> cars = carRepository.findAllById(ids);

        for (Car car : cars) {
            objectMapper.readerForUpdating(car).readValue(patchNode);
        }

        List<Car> resultCars = carRepository.saveAll(cars);
        return resultCars.stream()
                .map(Car::getId)
                .toList();
    }

    public Car delete(UUID id) {
        Car car = carRepository.findById(id).orElse(null);
        if (car != null) {
            carRepository.delete(car);
        }
        return car;
    }

    public void deleteMany(List<UUID> ids) {
        carRepository.deleteAllById(ids);
    }
}
