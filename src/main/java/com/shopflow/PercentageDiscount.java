
package com.shopflow;

public class PercentageDiscount implements Discount {

    private final double percentage;

    public PercentageDiscount(double percentage) {
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Percentage must be greater than 0 and at most 100."
            );
        }

        this.percentage = percentage;
    }

    @Override
    public String getName() {
        return percentage + "% Percentage Discount";
    }

    @Override
    public double calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        double discount = order.getTotal() * percentage / 100.0;

        return Math.min(discount, order.getTotal());
    }
}
