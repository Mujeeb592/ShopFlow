
package com.shopflow;

public class FlatDiscount implements Discount {

    private final double amount;

    public FlatDiscount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Discount amount must be greater than zero."
            );
        }

        this.amount = amount;
    }

    @Override
    public String getName() {
        return "Flat Discount (Rs. " + amount + ")";
    }

    @Override
    public double calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        return Math.min(amount, order.getTotal());
    }
}
