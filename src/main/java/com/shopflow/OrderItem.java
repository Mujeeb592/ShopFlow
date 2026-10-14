
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderItem {

    private final Product product;
    private final int quantity;
    private final BigDecimal priceAtPurchase;
    private final BigDecimal lineTotal;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null."
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();

        this.lineTotal = priceAtPurchase
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
