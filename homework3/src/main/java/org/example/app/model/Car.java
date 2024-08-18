package org.example.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Car {

    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String condition;
    public Car() {
        // Конструктор без параметров
    }
    @JsonCreator
    public Car(@JsonProperty("brand")String brand, @JsonProperty("model")String model,@JsonProperty("year") int year, @JsonProperty("price")double price,@JsonProperty("condition") String condition) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }  
    public Car( int id, String brand, String model, int year, double price, String condition) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }


    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getCondition() { return condition; }

    public void setId(int id) { this.id = id; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setCondition(String condition) { this.condition = condition; }
}
