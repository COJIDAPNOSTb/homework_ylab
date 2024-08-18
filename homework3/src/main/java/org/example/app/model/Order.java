package org.example.app.model;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Order {
    private int id;
    private int carId;
    private int customerId;
    private Car car;
    private User customer;
    private String type;
    private String status;
    private LocalDate date;
    public Order()
    {
        
    }
    public Order(int id, int customerId, int carId, LocalDate date, String type, String status) {
        this.id = id;
        this.carId = carId;
        this.customerId = customerId;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    @JsonCreator
    public Order(@JsonProperty("car_id") int carId, @JsonProperty("customer_id") int customerId, @JsonProperty("type") String type) {
        this.carId = carId;
        this.customerId = customerId;
        this.type = type;
        this.status = "Pending";
        this.date = LocalDate.now();
    }

    public int getId() { return id; }
    public int getCarId() {
        return carId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public Car getCar() { return car; }
    public User getCustomer() { return customer; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public LocalDate getDate() { return date; }

    public void setStatus(String status) { this.status = status; }
    public void setId(int id) {
        this.id = id;
    }
    public void setCar(Car car) {
        this.car = car;
    }
    public void setCustomer(User customer) {
        this.customer = customer;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setType(String type) {
        this.type = type;
    }public void setDate(LocalDate date) {
        this.date = date;
    }
}
