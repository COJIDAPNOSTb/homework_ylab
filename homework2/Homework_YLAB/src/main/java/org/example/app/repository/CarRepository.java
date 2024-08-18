package org.example.app.repository;


import java.util.List;

import org.example.app.model.Car;

public interface CarRepository {
    void save(Car car);
    void update(Car car);
    void delete(int carId);
    Car findById(int carId);
    List<Car> findAll();
}

