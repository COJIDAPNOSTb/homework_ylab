package org.example;

import org.example.app.persistence.*;
import org.example.app.config.ConfigDb;

import org.example.app.repository.*;
import org.example.app.service.CarService;
import org.example.app.service.OrderService;
import org.example.app.service.UserService;
import org.example.in.ConsoleController;
import org.example.in.UserInput;
import org.example.out.ConsoleView;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

import java.sql.SQLException;

public class Main {
     public static void main(String[] args) throws LiquibaseException {
        ConfigDb config = new ConfigDb();


        try (Connection connection = config.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase("db/changelog/db.changelog-main.xml", 
                new ClassLoaderResourceAccessor(), database);
            liquibase.update(""); 
            
            UserRepository userRepository = new JdbcUserRepository(config);

            CarRepository carRepository = new JdbcCarRepository(config);

            OrderRepository orderRepository = new JdbcOrderRepository(userRepository, carRepository, config);

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
