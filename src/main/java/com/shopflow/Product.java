
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
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
            throw new IllegalArgumentException(
                    "Product code cannot be empty."
            );
        }

        String cleanCode = code.trim();

        if (!cleanCode.matches("[A-Za-z0-9-]+")) {
            throw new IllegalArgumentException(
                    "Product code can contain letters, digits, and hyphens only."
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty."
            );
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Product category cannot be empty."
            );
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Product price must be greater than zero."
            );
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Product quantity cannot be negative."
            );
        }

        BigDecimal roundedPrice =
                price.setScale(2, RoundingMode.HALF_UP);

        if (roundedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Product price must be at least Rs. 0.01 after rounding."
            );
        }

        this.code = cleanCode;
        this.name = name.trim();
        this.category = category.trim();
        this.price = roundedPrice;
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
     * Two products are equal when their codes match,
     * ignoring letter case.
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
        return Objects.hash(code.toUpperCase(Locale.ROOT));
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
