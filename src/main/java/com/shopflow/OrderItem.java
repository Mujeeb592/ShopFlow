
package com.shopflow;

public class OrderItem {

    private final Product product;
    private final int quantity;
    private final double priceAtPurchase;
    private final double lineTotal;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();
        this.lineTotal = priceAtPurchase * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    @Override
    public String toString() {
        return String.format(
                "%-20s %5d x %8.2f = %9.2f",
                product.getName(),
                quantity,
                priceAtPurchase,
                lineTotal
        );
    }
}
