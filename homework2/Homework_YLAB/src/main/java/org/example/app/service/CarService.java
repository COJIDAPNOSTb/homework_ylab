package org.example.app.service;

import org.example.app.model.Car;
import org.example.app.model.User;
import org.example.app.repository.CarRepository;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Сервис, отвечающий за логику управления автомобилями
 */
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
/**
 * Конструктор
 * @param carRepository репозиторий
 * @param userService сервис для управления пользователями
 */
    public CarService(CarRepository carRepository, UserService userService) {
        this.carRepository = carRepository;
        this.userService = userService;
    }
/**
 * Метод отвечающий за добавление нового автомобиля
 * @param user пользователь, необходим для логирования
 * @param brand бренд автомобиля
 * @param model модель автомобиля
 * @param year год выпуска
 * @param price цена
 * @param condition состояние
 */
    public void addCar(User user, String brand, String model, int year, double price, String condition) {
        Car car = new Car(brand, model, year, price, condition);
        carRepository.save(car);
        userService.logAction(user, "Added car: " + car.getId());
    }
/**
 * Метод отвечающий за изменение автомобиля
 * @param user пользователь, необходим для логирования
 * @param carId id автомобиля
 * @param brand бренд автомобиля
 * @param model модель автомобиля
 * @param year год выпуска
 * @param price цена
 * @param condition состояние
 */
    public void editCar(User user, int carId, String brand, String model, int year, double price, String condition) {
        Car car = carRepository.findById(carId);
        if (car != null) {
            car.setBrand(brand);
            car.setModel(model);
            car.setYear(year);
            car.setPrice(price);
            car.setCondition(condition);
            carRepository.update(car);
            userService.logAction(user, "Edited car: " + car.getId());
        }
    }
/**
 * Метод отвечающий за удаление автомобиля
 * @param user пользователь
 * @param carId id автомобиля
 */
    public void deleteCar(User user, int carId) {
        carRepository.delete(carId);
        userService.logAction(user, "Deleted car: " + carId);
    }
/**
 * Метод отвечающий за списко автомобилей
 * @return лист автомобилей
 */
    public List<Car> listCars() {
        return carRepository.findAll();
    }
    /**
     * Метод отвечающий за поиск автомобиля по критериям
     * @param brand критерий
     * @param model критерий
     * @param year критерий 
     * @param price критерий
     * @param condition критерий
     * @return возвращает отфильтроанный список
     */
    public List<Car> searchCars(String brand, String model, Integer year, Double price, String condition) {
        return carRepository.findAll().stream()
                .filter(car -> (brand == null || car.getBrand().equalsIgnoreCase(brand)) &&
                               (model == null || car.getModel().equalsIgnoreCase(model)) &&
                               (year == null || car.getYear() == year) &&
                               (price == null || car.getPrice() <= price) &&
                               (condition == null || car.getCondition().equalsIgnoreCase(condition))).collect(Collectors.toList());
    }
    /**
     * Поиск авто по id
     * @param id
     * @return авто с выбранным id
     */
     public Car findCarById(int id) {
        return carRepository.findById(id);
    }
}
