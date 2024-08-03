package org.example.app.persistence;



import org.example.app.model.Car;
import org.example.app.repository.CarRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса CarRepository
 */
public class InMemoryCarRepository implements CarRepository {
    private final List<Car> cars = new ArrayList<>();

    @Override
    public void save(Car car) {
        cars.add(car);
    }

    @Override
    public void update(Car car) {
        int index = findIndexById(car.getId());
        if (index != -1) {
            cars.set(index, car);
        }
    }

    @Override
    public void delete(int carId) {
        cars.removeIf(car -> car.getId() == carId);
    }

    @Override
    public Car findById(int carId) {
        return cars.stream().filter(car -> car.getId() == carId).findFirst().orElse(null);
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    private int findIndexById(int carId) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId() == carId) {
                return i;
            }
        }
        return -1;
    }
}
