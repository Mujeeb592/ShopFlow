
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

    public Order(int id, Customer customer) {
        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
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

    public void printReceipt() {
        System.out.println("\n==============================================");
        System.out.println("                 SHOPFLOW");
        System.out.println("                ORDER RECEIPT");
        System.out.println("==============================================");

        System.out.println("Order ID: " + id);
        System.out.println("Date: " + date);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());

        System.out.println("----------------------------------------------");
        System.out.printf("%-20s %5s %10s %10s%n",
                "Product", "Qty", "Price", "Total");
        System.out.println("----------------------------------------------");

        for (OrderItem item : items) {
            System.out.printf("%-20s %5d %10.2f %10.2f%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtPurchase(),
                    item.getLineTotal());
        }

        System.out.println("----------------------------------------------");
        System.out.printf("ORDER TOTAL: Rs. %.2f%n", getTotal());
        System.out.println("==============================================");
    }
}
