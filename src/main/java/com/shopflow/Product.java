package com.shopflow;
public class Product {
    private String code;
    private String name;
    private double price;
    private int quantity;
    public Product(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void displayProduct() {
        System.out.println(
                "Code: " + code +
                " | Name: " + name +
                " | Price: " + price +
                " | Quantity: " + quantity
        );
    }
}
