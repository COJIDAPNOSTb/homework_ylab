package org.example;

import org.example.app.persistence.*;
import org.example.app.service.*;
import org.example.in.ConsoleController;
import org.example.in.UserInput;
import org.example.out.ConsoleView;
/**
 * Основной класс приложения, отвечающий за его работу
 */
public class Main {

    public static void main(String[] args) {
     
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
        UserService userService = new UserService(new InMemoryUserRepository());
        CarService carService = new CarService(new InMemoryCarRepository(), userService);
        OrderService orderService = new OrderService(new InMemoryOrderRepository(), userService);
        
        ConsoleView consoleView = new ConsoleView(userInput);
        ConsoleController controller = new ConsoleController(userService, carService, orderService, consoleView);
        controller.run();
    }
}