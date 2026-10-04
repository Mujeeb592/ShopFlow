package com.shopflow;

public class Product {

    private String code;
    private String name;
    private String category;
    private double price;
    private int quantity;

    public Product(String code, String name, String category,
                   double price, int quantity) {

        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getStockValue() {
        return price * quantity;
    }

    public void displayProduct() {
        System.out.printf(
                "%-10s %-20s %-20s %10.2f %10d%n",
                code,
                name,
                category,
                price,
                quantity
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}