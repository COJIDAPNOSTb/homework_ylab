package org.example.app.model;

import java.time.LocalDate;
/**Заказ */
public class Order {
    private static int counter = 0;
    private final int id;
    private final Car car;
    private final User customer;
    private final String type;
    private String status;
    private final LocalDate date;
/**
 * Конструктор
 * @param car автомобиль
 * @param customer покупатель
 * @param type тип
 */
    public Order(Car car, User customer, String type) {
        this.id = ++counter;
        this.car = car;
        this.customer = customer;
        this.type = type;
        this.status = "Pending";
        this.date = LocalDate.now();  
    }
/**
 * Геттеры
 * 
 */
    public int getId() { return id; }
    public Car getCar() { return car; }
    public User getCustomer() { return customer; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public LocalDate getDate() { return date; }

    public void setStatus(String status) { this.status = status; }
}
