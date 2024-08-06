package org.example.out;


import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.User;
import org.example.in.UserInput;

import java.util.List;

/**
 * Класс отвечает за вывод информации на коноль
 */

public class ConsoleView {
    private final UserInput userInput;
/**
 * Конструктор 
 * @param userInput отвечает за передачу пользовательского ввода
 */
    public ConsoleView(UserInput userInput) {
        this.userInput = userInput;
    }
/**Метод, отвечающий за вывод меню регистрации 
 * @return целочисленное значение от пользователя
*/
    public int displayMainMenuAndGetChoice() {
        System.out.println("1. Register\n2. Login\n0. Exit");
        return userInput.getIntInput();
    }
/**
 * Метод отвечающий за вывод меню в зависимости от роли текущего пользователя
 * @param currentUser получает текущего пользователя для выявления его роли
 * @return целочисленное значение от пользователя
 */ 
    public int displayMenuAndGetChoice(User currentUser) {
        switch (currentUser.getRole()) {
            case ADMIN -> System.out.println(
                "1. Add Car\n2. Edit Car\n3. Delete Car\n4. List Cars\n5. Create Order\n6. Update Order Status\n7. Delete Order\n8. List Orders\n9. List Users\n10. Edit User\n11. Delete User\n12. View Audit Logs\n13. Export Audit Logs\n14. Search Cars\n15. Search Orders\n0. Exit");
            case MANAGER -> System.out.println(
                "1. Add Car\n2. Edit Car\n3. Delete Car\n4. List Cars\n5. Create Order\n6. List Orders\n7. Search Cars\n8. Search Orders\n0. Exit");
            case CUSTOMER -> System.out.println(
                "1. List Cars\n2. Create Order\n3. List Orders\n4. Search Cars\n0. Exit");
        }
        return userInput.getIntInput();
    }
/**
 * Метод для передачи строковых сообщений 
 * @param prompt строка пользователя
 * @return строковое значение
 */
    public String getInput(String prompt) {
        System.out.print(prompt);
        return userInput.getStringInput();
    }

/**
 * Метод для передачи целочисленных сообщений 
 * @param prompt строка пользователя
 * @return целочисленное значение
 */
    public int getIntInput(String prompt) {
        System.out.print(prompt);
        return userInput.getIntInput();
    }
/**
 * Метод для передачи сообщений с плавающей точкой
 * @param prompt строка пользователя
 * @return значение с плавающей точкой
 */
    public double getDoubleInput(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(userInput.getStringInput());
    }
/**
 * Метод отвечающий за вывод сообщений
 * @param message сообщение
 */
    public void displayMessage(String message) {
        System.out.println(message);
    }
/**
 * Метод отвечающий за отображение списка автомобилей
 * @param cars Лист автомобилей
 */
    public void displayCars(List<Car> cars) {
        for (Car car : cars) {
            System.out.println(car.getId() + " - " + car.getBrand() + " " + car.getModel() + " (" + car.getYear() + "), " + car.getPrice() + " USD, " + car.getCondition());
        }
    }
/**
 * Метод отвечающий за отображение списка заказов
 * @param orders список заказов
 */
    public void displayOrders(List<Order> orders) {
        for (Order order : orders) {
            System.out.println(order.getId() + " - Car: " + order.getCar().getBrand() + " " + order.getCar().getModel() + ", Customer: " + order.getCustomer().getUsername() + ", Status: " + order.getStatus() + ", Date: " + order.getDate() + ", Type: " + order.getType());
        }
    }
/**
 * Метод, отвечающий за отображение списка пользователей
 * @param users список пользователей
 */
    public void displayUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user.getId() + " - " + user.getUsername() + " (" + user.getRole() + ")");
        }
    }
}


