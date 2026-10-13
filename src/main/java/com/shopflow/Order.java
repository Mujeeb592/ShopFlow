
package com.shopflow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final int id;
    private final Customer customer;
    private final LocalDate date;
    private final List<OrderItem> items;
    private OrderStatus status;

    private Discount appliedDiscount;
    private double discountAmount;

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
        this.discountAmount = 0;
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

    public double getDiscountAmount() {
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

    public double getTotal() {
        double total = 0;

        for (OrderItem item : items) {
            total += item.getLineTotal();
        }

        return total;
    }

    public void applyDiscount(Discount discount) {
        if (discount == null) {
            throw new IllegalArgumentException(
                    "Discount cannot be null."
            );
        }

        double calculatedDiscount = discount.calculateDiscount(this);

        if (calculatedDiscount < 0) {
            throw new IllegalArgumentException(
                    "Discount cannot be negative."
            );
        }

        this.appliedDiscount = discount;
        this.discountAmount = Math.min(
                calculatedDiscount,
                getTotal()
        );
    }

    public double getFinalTotal() {
        return Math.max(0, getTotal() - discountAmount);
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
            System.out.printf("%-18s %5d %9.2f %10.2f%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtPurchase(),
                    item.getLineTotal());
        }

        System.out.println("----------------------------------------------");
        System.out.printf("SUBTOTAL:          Rs. %.2f%n", getTotal());

        if (appliedDiscount != null) {
            System.out.println(
                    "Discount:          " + appliedDiscount.getName()
            );
            System.out.printf(
                    "Discount Amount:   Rs. %.2f%n",
                    discountAmount
            );
        } else {
            System.out.println("Discount:          None");
            System.out.printf(
                    "Discount Amount:   Rs. %.2f%n",
                    0.0
            );
        }

        System.out.printf("FINAL TOTAL:       Rs. %.2f%n", getFinalTotal());
        System.out.println("==============================================");
    }
}
