
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscount implements Discount {

    private final BigDecimal percentage;

    public PercentageDiscount(BigDecimal percentage) {
        if (percentage == null
                || percentage.compareTo(BigDecimal.ZERO) <= 0
                || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    "Percentage must be greater than 0 and at most 100."
            );
        }

        this.percentage = percentage;
    }

    @Override
    public String getName() {
        return percentage.stripTrailingZeros().toPlainString()
                + "% Percentage Discount";
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        BigDecimal discount = order.getTotal()
                .multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return discount.min(order.getTotal())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
