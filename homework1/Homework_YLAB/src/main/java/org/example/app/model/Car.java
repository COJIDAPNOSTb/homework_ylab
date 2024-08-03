package org.example.app.model;

/**атомобиль */
public class Car {
    private static int counter = 0;
    private final int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String condition;
/**
 * Конструктор
 * @param brand бренд
 * @param model модель
 * @param year год
 * @param price цена
 * @param condition состояние
 */
    public Car(String brand, String model, int year, double price, String condition) {
        this.id = ++counter;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }
/**
 * Геттеры
 */ 
    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getCondition() { return condition; }

/**
 * Сеттеры
 */ 
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setCondition(String condition) { this.condition = condition; }
}
