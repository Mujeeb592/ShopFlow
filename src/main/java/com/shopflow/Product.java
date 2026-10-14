
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    private String code;
    private String name;
    private String category;
    private BigDecimal price;
    private int quantity;

    public Product(String code, String name, String category,
                   BigDecimal price, int quantity) {

        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
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

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean reduceStock(int quantityToReduce) {
        if (quantityToReduce <= 0) {
            return false;
        }

        if (quantityToReduce > this.quantity) {
            return false;
        }

        this.quantity -= quantityToReduce;
        return true;
    }

    public BigDecimal getStockValue() {
        return price.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void displayProduct() {
        System.out.printf(
                "%-10s %-20s %-20s %10s %10d%n",
                code,
                name,
                category,
                price.toPlainString(),
                quantity
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price.toPlainString() +
                ", quantity=" + quantity +
                '}';
    }
}
