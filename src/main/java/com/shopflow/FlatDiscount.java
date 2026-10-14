
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FlatDiscount implements Discount {

    private final BigDecimal amount;

    public FlatDiscount(BigDecimal amount) {
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Discount amount must be greater than zero."
            );
        }

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getName() {
        return "Flat Discount (Rs. " + amount.toPlainString() + ")";
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        return amount.min(order.getTotal())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
