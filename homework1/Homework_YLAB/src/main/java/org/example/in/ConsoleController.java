
package org.example.in;

import org.example.app.log.AuditLog;
import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.service.*;


import java.util.List;

import org.example.out.ConsoleView;


import java.time.format.DateTimeFormatter;



import java.time.LocalDate;
/*основной класс управления консольным приложением */

public class ConsoleController {
    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final ConsoleView consoleView;
    private User currentUser;
    private boolean running = true;
 /**
     * Конструктор 
      
    @param userService сервис для управления пользователями
    @param carService сервис для управления автомобилями
    @param orderService сервис для управления заказами
    @param consoleView класс для вывода информации в консоль и получения ввода от пользователя
     */
    public ConsoleController(UserService userService, CarService carService, OrderService orderService, ConsoleView consoleView) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.consoleView = consoleView;
    }
/*
Цикл для работы приложения 
*/
    public void run() {
        while (running) {
            if (currentUser == null) {
                int choice = consoleView.displayMainMenuAndGetChoice();
                choiseForLogin(choice);
            } else {
                int choice = consoleView.displayMenuAndGetChoice(currentUser);
                handleChoice(choice);
            }
        }
    }
    
/*
Метод, останавливающий работу приложения 
*/
    public void stop() {
        running = false;
    }
    
/**
Метод, отвечающий за управление меню регистрации
@param choise отвечает за выбор пользователя 
*/
    private void choiseForLogin(int choice) {
        switch (choice) {
            case 1 -> registerUser();
            case 2 -> loginUser();
            case 0 -> stop();
            default -> consoleView.displayMessage("Invalid choice.");
        }
    }

   /**
Метод, отвечающий за управление меню регистрации
@param choise отвечает за выбор пользователя 
*/
    private void handleChoice(int choice) {
        switch (currentUser.getRole()) {
            case ADMIN -> handleAdminChoice(choice);
            case MANAGER -> handleManagerChoice(choice);
            case CUSTOMER -> handleCustomerChoice(choice);
        }
    }

      /**
Метод, отвечающий за управление меню администратора
* @param choise отвечает за выбор пользователя 
*/
    
    private void handleAdminChoice(int choice) {
        switch (choice) {
            case 1 -> addCar();
            case 2 -> editCar();
            case 3 -> deleteCar();
            case 4 -> listCars();
            case 5 -> createOrder();
            case 6 -> updateOrderStatus();
            case 7 -> deleteOrder();
            case 8 -> listOrders();
            case 9 -> listUsers();
            case 10 -> editUser();
            case 11 -> deleteUser();
            case 12 -> viewAuditLogs();
            case 13 -> exportAuditLogs();
            case 14 -> searchCars();
            case 15 -> searchOrders();
            case 0 -> logout();
            default -> consoleView.displayMessage("Invalid choice.");
        }
    }
      /**
Метод, отвечающий за управление меню менеджера
* @param choise отвечает за выбор пользователя 
*/
    private void handleManagerChoice(int choice) {
        switch (choice) {
            case 1 -> addCar();
            case 2 -> editCar();
            case 3 -> deleteCar();
            case 4 -> listCars();
            case 5 -> createOrder();
            case 6 -> listOrders();
            case 7 -> searchCars();
            case 8 -> searchOrders();
            case 0 -> logout();
            default -> consoleView.displayMessage("Invalid choice.");
        }
    }
      /**
Метод, отвечающий за управление меню покупателя
* @param choise отвечает за выбор пользователя 
*/
    private void handleCustomerChoice(int choice) {
        switch (choice) {
            case 1 -> listCars();
            case 2 -> createOrder();
            case 3 -> listOrders();
            case 4 -> searchCars();
            case 0 -> logout();
            default -> consoleView.displayMessage("Invalid choice.");
        }
    }
      /**
Метод, отвечающий за регитсрацию пользователя. После успешной регистрации,
    пользователь будет автоматически залогинен под зарегестрированным аккаунтом 
*/
private void registerUser() {
    String username;
    String password;
    
    do {
        username = consoleView.getInput("Username: ");
        if (userService.findUserByUsername(username) != null) {
            consoleView.displayMessage("Username already exists. Please choose a different username.");
        }
    } while (userService.findUserByUsername(username) != null);

    password = consoleView.getInput("Password: ");
    
    int roleChoice;
    do {
        roleChoice = consoleView.getIntInput("Role:\n1. ADMIN\n2. MANAGER\n3. CUSTOMER\nChoose role: ");
        if (roleChoice < 1 || roleChoice > 3) {
            consoleView.displayMessage("Invalid choice. Please choose a valid role.");
        }
    } while (roleChoice < 1 || roleChoice > 3);

    Role role;
    switch (roleChoice) {
        case 1 -> role = Role.ADMIN;
        case 2 -> role = Role.MANAGER;
        case 3 -> role = Role.CUSTOMER;
        default -> throw new IllegalStateException("Unexpected value: " + roleChoice);
    }
    
    userService.register(username, password, role);
    currentUser = userService.login(username, password);
}


    private void loginUser() {
        String username = consoleView.getInput("Username: ");
        String password = consoleView.getInput("Password: ");
        currentUser = userService.login(username, password);
        if (currentUser == null) {
            consoleView.displayMessage("Invalid credentials.");
        } else {
            consoleView.displayMessage("Logged in as " + currentUser.getUsername());
        }
    }

    private void logout() {
        currentUser = null;
    }

    private void addCar() {
        if (currentUser == null || currentUser.getRole() == Role.CUSTOMER) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        String brand = consoleView.getInput("Brand: ");
        String model = consoleView.getInput("Model: ");
        int year = Integer.parseInt(consoleView.getInput("Year: "));
        double price = Double.parseDouble(consoleView.getInput("Price: "));
        String condition = consoleView.getInput("Condition: ");
        carService.addCar(currentUser, brand, model, year, price, condition);
    }

    private void editCar() {
        if (currentUser == null || currentUser.getRole() == Role.CUSTOMER) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int carId = Integer.parseInt(consoleView.getInput("Car ID: "));
        String brand = consoleView.getInput("Brand: ");
        String model = consoleView.getInput("Model: ");
        int year = Integer.parseInt(consoleView.getInput("Year: "));
        double price = Double.parseDouble(consoleView.getInput("Price: "));
        String condition = consoleView.getInput("Condition: ");
        carService.editCar(currentUser, carId, brand, model, year, price, condition);
    }

    private void deleteCar() {
        if (currentUser == null || currentUser.getRole() == Role.CUSTOMER) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int carId = Integer.parseInt(consoleView.getInput("Car ID: "));
        carService.deleteCar(currentUser, carId);
    }

    private void listCars() {
        consoleView.displayCars(carService.listCars());
    }

    private void createOrder() {
        if (currentUser == null || currentUser.getRole() == Role.ADMIN) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int carId = Integer.parseInt(consoleView.getInput("Car ID: "));
        String orderType = consoleView.getInput("Order Type (Purchase/Service): ");
        var car = carService.listCars().stream().filter(c -> c.getId() == carId).findFirst().orElse(null);
        if (car != null) {
            orderService.createOrder(currentUser, car, orderType);
        } else {
            consoleView.displayMessage("Car not found.");
        }
    }

    private void updateOrderStatus() {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int orderId = Integer.parseInt(consoleView.getInput("Order ID: "));
        String status = consoleView.getInput("New Status: ");
        orderService.updateOrderStatus(currentUser, orderId, status);
    }

    private void deleteOrder() {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int orderId = Integer.parseInt(consoleView.getInput("Order ID: "));
        orderService.deleteOrder(currentUser, orderId);
    }

    private void listOrders() {
        consoleView.displayOrders(orderService.listOrders());
    }

    private void listUsers() {
        consoleView.displayUsers(userService.listUsers());
    }

    private void editUser() {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int userId = Integer.parseInt(consoleView.getInput("User ID: "));
        String username = consoleView.getInput("Username: ");
        String password = consoleView.getInput("Password: ");
        String role = consoleView.getInput("Role (ADMIN/MANAGER/CUSTOMER): ");
        userService.updateUser(userId, username, password, Role.valueOf(role));
    }

    private void deleteUser() {
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            consoleView.displayMessage("Unauthorized action.");
            return;
        }
        int userId = Integer.parseInt(consoleView.getInput("User ID: "));
        userService.deleteUser(userId);
    }

    private void viewAuditLogs() {
        for (AuditLog log : userService.getAuditLogs()) {
            consoleView.displayMessage(log.getTimestamp() + " - " + log.getUser().getUsername() + " - " + log.getAction());
        }
    }

    private void exportAuditLogs() {
        userService.exportAuditLogs();
    }

    private void searchCars() {
        String brand = consoleView.getInput("Brand (leave empty for any): ");
        String model = consoleView.getInput("Model (leave empty for any): ");
        int year = Integer.parseInt(consoleView.getInput("Year (0 for any): "));
        double price = Double.parseDouble(consoleView.getInput("Price (0 for any): "));
        String condition = consoleView.getInput("Condition (leave empty for any): ");

        List<Car> results = carService.searchCars(
                brand.isEmpty() ? null : brand,
                model.isEmpty() ? null : model,
                year == 0 ? null : year,
                price == 0 ? null : price,
                condition.isEmpty() ? null : condition
        );

        consoleView.displayCars(results);
    }

    private void searchOrders() {
        String dateStr = consoleView.getInput("Date (YYYY-MM-DD or leave empty for any): ");
        String customer = consoleView.getInput("Customer (leave empty for any): ");
        String status = consoleView.getInput("Status (leave empty for any): ");
        String carModel = consoleView.getInput("Car Model (leave empty for any): ");

        LocalDate date = null;
        if (!dateStr.isEmpty()) {
            try {
                date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                consoleView.displayMessage("Invalid date format.");
                return;
            }
        }

        List<Order> results = orderService.searchOrders(
                date,
                customer.isEmpty() ? null : customer,
                status.isEmpty() ? null : status,
                carModel.isEmpty() ? null : carModel
        );

        consoleView.displayOrders(results);
    }
}
