
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode ROUNDING_RULE = RoundingMode.HALF_UP;

    private final int id;
    private final Customer customer;
    private final LocalDate date;
    private final List<OrderItem> items;
    private OrderStatus status;

    private Discount appliedDiscount;
    private BigDecimal discountAmount;

    public Order(int id, Customer customer) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Order ID must be positive."
            );
        }

        if (customer == null) {
            throw new IllegalArgumentException(
                    "An order must have a customer."
            );
        }

        this.id = id;
        this.customer = customer;
        this.date = LocalDate.now();
        this.items = new ArrayList<>();
        this.status = OrderStatus.NEW;
        this.appliedDiscount = null;
        this.discountAmount = BigDecimal.ZERO.setScale(MONEY_SCALE);
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Discount getAppliedDiscount() {
        return appliedDiscount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void changeStatus(OrderStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    status.getTransitionFailureReason(newStatus)
            );
        }

        if (status == OrderStatus.PAID
                && newStatus == OrderStatus.CANCELLED) {
            System.out.println(
                    "Reminder: A refund is required for this cancellation."
            );
        }

        status = newStatus;
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Order item cannot be null."
            );
        }

        items.add(item);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO.setScale(MONEY_SCALE);

        for (OrderItem item : items) {
            total = total.add(item.getLineTotal());
        }

        return total.setScale(MONEY_SCALE, ROUNDING_RULE);
    }

    public void applyDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException(
                    "Discount cannot be null."
            );
        }

        BigDecimal calculatedDiscount = discount.calculateDiscount(this)
                .setScale(MONEY_SCALE, ROUNDING_RULE);

        if (calculatedDiscount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Discount cannot be negative."
            );
        }

        this.appliedDiscount = discount;

        this.discountAmount = calculatedDiscount.min(getTotal())
                .setScale(MONEY_SCALE, ROUNDING_RULE);
    }

    public BigDecimal getFinalTotal() {
        return getTotal()
                .subtract(discountAmount)
                .max(BigDecimal.ZERO.setScale(MONEY_SCALE))
                .setScale(MONEY_SCALE, ROUNDING_RULE);
    }

    public void printReceipt() {
        System.out.println("\n==============================================");
        System.out.println("                  SHOPFLOW");
        System.out.println("                 ORDER RECEIPT");
        System.out.println("==============================================");

        System.out.println("Order ID: " + id);
        System.out.println("Date: " + date);
        System.out.println("Customer ID: " + customer.getId());
        System.out.println("Customer: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("Status: " + status);

        System.out.println("----------------------------------------------");
        System.out.printf("%-18s %5s %9s %10s%n",
                "Product", "Qty", "Price", "Total");
        System.out.println("----------------------------------------------");

        for (OrderItem item : items) {
            System.out.printf("%-18s %5d %9s %10s%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtPurchase().toPlainString(),
                    item.getLineTotal().toPlainString());
        }

        System.out.println("----------------------------------------------");
        System.out.println("SUBTOTAL:          Rs. " + getTotal().toPlainString());

        if (appliedDiscount != null) {
            System.out.println(
                    "Discount:          " + appliedDiscount.getName()
            );
        } else {
            System.out.println("Discount:          None");
        }

        System.out.println(
                "Discount Amount:   Rs. " + discountAmount.toPlainString()
        );

        System.out.println(
                "FINAL TOTAL:       Rs. " + getFinalTotal().toPlainString()
        );

        System.out.println("==============================================");
    }
}
