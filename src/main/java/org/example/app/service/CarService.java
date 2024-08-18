package org.example.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.app.dto.CarDTO;
import org.example.app.mapper.CarMapper;
import org.example.app.model.Car;
import org.example.app.repository.CarRepository;

public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll()
                             .stream()
                             .map(carMapper::toDTO)
                             .collect(Collectors.toList());
    }

    public CarDTO getCarById(int id) {
        Car car = carRepository.findById(id);
        return car != null ? carMapper.toDTO(car) : null;
    }

    public void saveCar(CarDTO carDTO) {
        Car car = carMapper.toEntity(carDTO);
        carRepository.save(car);
    }

    public void updateCar(int id, CarDTO carDTO) {
        Car car = carMapper.toEntity(carDTO);
        car.setId(id);
        carRepository.update(car);
    }

    public void deleteCar(int id) {
        carRepository.delete(id);
    }
}
