
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product {

    private final String code;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private int quantity;

    public Product(String code, String name, String category,
                   BigDecimal price, int quantity) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Product code cannot be empty.");
        }

        this.code = code.trim();
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
        if (quantityToReduce <= 0 || quantityToReduce > quantity) {
            return false;
        }

        quantity -= quantityToReduce;
        return true;
    }

    public BigDecimal getStockValue() {
        return price.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void displayProduct() {
        System.out.printf(
                "%-10s %-20s %-20s %10s %10d%n",
                code, name, category, price.toPlainString(), quantity
        );
    }

    /*
     * BUSINESS RULE:
     * Two products are the same if their product codes match,
     * ignoring letter case. For example, KB-100 and kb-100
     * represent the same product, regardless of price or stock.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product other = (Product) obj;

        return code.equalsIgnoreCase(other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code.toUpperCase(java.util.Locale.ROOT));
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
