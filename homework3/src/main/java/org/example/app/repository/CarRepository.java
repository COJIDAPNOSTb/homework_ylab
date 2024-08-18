package org.example.app.repository;

import org.example.app.model.Car;
import java.util.List;
public interface CarRepository {
     void save(Car car);
    void update(Car car);
    void delete(int carId);
    Car findById(int carId);
    List<Car> findAll();
}
