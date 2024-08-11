package org.example;

import org.example.app.persistence.*;
import org.example.app.config.ConfigDb;
import org.example.app.model.*;
import org.example.app.repository.*;
import org.example.app.service.CarService;
import org.example.app.service.OrderService;
import org.example.app.service.UserService;
import org.example.in.ConsoleController;
import org.example.in.UserInput;
import org.example.out.ConsoleView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
     public static void main(String[] args) {
        // Настройки подключения к базе данных
        ConfigDb config = new ConfigDb();

        // Используем конфигурацию для подключения к базе данных
        String url = config.getDbUrl();
        String username = config.getDbUsername();
        String password = config.getDbPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Создание репозиториев
            UserRepository userRepository = new JdbcUserRepository(url, username, password);
            CarRepository carRepository = new JdbcCarRepository(url, username, password);
            OrderRepository orderRepository = new JdbcOrderRepository(url, username, password, userRepository, carRepository);

                   UserInput userInput = new UserInput() {
        @SuppressWarnings("resource")
        @Override
        public int getIntInput() {
            
            return new java.util.Scanner(System.in).nextInt();
        }

        @SuppressWarnings("resource")
        @Override
        public String getStringInput() {

            return new java.util.Scanner(System.in).nextLine();
        }
    };

            UserService userService = new UserService(userRepository);
            CarService carService = new CarService(carRepository,userService);
            OrderService orderService = new OrderService(carRepository, orderRepository,userService);

         
            ConsoleView consoleView = new ConsoleView(userInput);
            ConsoleController consoleController = new ConsoleController(userService, carService, orderService, consoleView);

            // Запуск приложения
            consoleController.run();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
