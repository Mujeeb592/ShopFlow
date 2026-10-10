
package com.shopflow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Product> products = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {

        // Sample products for testing
        products.add(new Product("101", "Keyboard", "Accessories", 1500, 10));
        products.add(new Product("102", "Mouse", "Accessories", 800, 15));
        products.add(new Product("103", "Monitor", "Electronics", 25000, 5));

        boolean running = true;

        while (running) {
            System.out.println("\n========== SHOPFLOW MENU ==========");
            System.out.println("1. View Products");
            System.out.println("2. Add Product");
            System.out.println("3. Search Product by Code");
            System.out.println("4. Register Customer");
            System.out.println("5. Place Order");
            System.out.println("6. View Order / Receipt");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewProducts();
                    break;
                case "2":
                    addProduct();
                    break;
                case "3":
                    searchProduct();
                    break;
                case "4":
                    registerCustomer();
                    break;
                case "5":
                    placeOrder();
                    break;
                case "6":
                    viewOrder();
                    break;
                case "7":
                    running = false;
                    System.out.println("Thank you for using ShopFlow!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-7.");
            }
        }

        scanner.close();
    }

    private static void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n---------------- PRODUCT LIST ----------------");
        System.out.printf("%-10s %-20s %-20s %10s %10s%n",
                "Code", "Name", "Category", "Price", "Stock");

        for (Product product : products) {
            product.displayProduct();
        }

        double stockValue = 0;
        for (Product product : products) {
            stockValue += product.getStockValue();
        }

        System.out.println("----------------------------------------------");
        System.out.println("Total products: " + products.size());
        System.out.printf("Total stock value: Rs. %.2f%n", stockValue);
    }

    private static void addProduct() {
        System.out.println("\n========== ADD PRODUCT ==========");

        String code = readNonEmpty("Enter numeric product code: ");

        if (!code.matches("\\d+")) {
            System.out.println("Product code must contain digits only.");
            return;
        }

        if (findProduct(code) != null) {
            System.out.println("Product code already exists.");
            return;
        }

        String name = readNonEmpty("Enter product name: ");
        String category = readNonEmpty("Enter category: ");

        double price = readPositivePrice("Enter price: ");
        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        int quantity = readNonNegativeInt("Enter opening stock: ");
        if (quantity < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        products.add(new Product(code, name, category, price, quantity));
        System.out.println("Product added successfully.");
    }

    private static void searchProduct() {
        String code = readNonEmpty("Enter product code to search: ");
        Product product = findProduct(code);

        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.printf("%-10s %-20s %-20s %10s %10s%n",
                    "Code", "Name", "Category", "Price", "Stock");
            product.displayProduct();
        }
    }

    private static void registerCustomer() {
        System.out.println("\n========== REGISTER CUSTOMER ==========");

        String name = readNonEmpty("Enter customer name: ");
        String email = readNonEmpty("Enter email: ");
        String phone = readNonEmpty("Enter phone: ");

        int id = customers.size() + 1;

        Customer customer = new Customer(id, name, email, phone);
        customers.add(customer);

        System.out.println("\nCustomer registered successfully!");
        System.out.println(customer);
    }

    private static void placeOrder() {
        System.out.println("\n========== PLACE ORDER ==========");

        if (customers.isEmpty()) {
            System.out.println(
                    "No customers registered. Register a customer first."
            );
            return;
        }

        if (products.isEmpty()) {
            System.out.println("No products available to order.");
            return;
        }

        System.out.println("\nRegistered Customers:");
        for (Customer customer : customers) {
            System.out.println(customer.getId() + ". "
                    + customer.getName() + " (" + customer.getEmail() + ")");
        }

        int customerId = readPositiveInt("Enter customer ID: ");
        if (customerId <= 0) {
            System.out.println("Invalid customer ID.");
            return;
        }

        Customer customer = findCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found. Order cancelled.");
            return;
        }

        viewProducts();

        System.out.println("\nEnter product codes and quantities.");
        System.out.println("Type DONE as the product code to finish.");

        // LinkedHashMap preserves selection order.
        // Selecting the same product twice combines its quantities.
        Map<Product, Integer> requestedItems = new LinkedHashMap<>();

        while (true) {
            System.out.print("\nProduct code (or DONE): ");
            String code = scanner.nextLine().trim();

            if (code.equalsIgnoreCase("DONE")) {
                break;
            }

            Product product = findProduct(code);

            if (product == null) {
                System.out.println("Product not found. Try again.");
                continue;
            }

            System.out.print("Enter quantity: ");
            String quantityInput = scanner.nextLine().trim();

            int quantity;
            try {
                quantity = Integer.parseInt(quantityInput);
            } catch (NumberFormatException e) {
                System.out.println("Quantity must be a whole number.");
                continue;
            }

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                continue;
            }

            int previousQuantity = requestedItems.getOrDefault(product, 0);

            if (quantity > product.getQuantity() - previousQuantity) {
                System.out.println(
                        "Insufficient stock. Available stock: "
                                + product.getQuantity()
                                + ", already requested: " + previousQuantity
                );
                continue;
            }

            requestedItems.put(product, previousQuantity + quantity);

            System.out.println("Item added to order.");
        }

        if (requestedItems.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        // Validate every product before changing any stock.
        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            if (entry.getValue() > entry.getKey().getQuantity()) {
                System.out.println(
                        "Order refused: insufficient stock for "
                                + entry.getKey().getName()
                );
                return;
            }
        }

        int orderId = orders.size() + 1;
        Order order = new Order(orderId, customer);

        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            order.addItem(new OrderItem(entry.getKey(), entry.getValue()));
        }

        // Reduce stock only after the complete order has been validated.
        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            boolean reduced = entry.getKey().reduceStock(entry.getValue());

            if (!reduced) {
                // Defensive check; normally validation prevents this.
                System.out.println(
                        "Stock update failed. Please review inventory."
                );
                return;
            }
        }

        orders.add(order);

        System.out.println("\nOrder placed successfully!");
        order.printReceipt();
    }

    private static void viewOrder() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n========== EXISTING ORDERS ==========");

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId()
                    + " | Customer: " + order.getCustomer().getName()
                    + " | Date: " + order.getDate()
                    + " | Total: Rs. " + String.format("%.2f", order.getTotal()));
        }

        int orderId = readPositiveInt("Enter order ID to view receipt: ");
        if (orderId <= 0) {
            System.out.println("Invalid order ID.");
            return;
        }

        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.printReceipt();
        }
    }

    private static Product findProduct(String code) {
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                return product;
            }
        }
        return null;
    }

    private static Customer findCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    private static Order findOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static int readPositiveInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int readNonNegativeInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            return value >= 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readPositivePrice(String prompt) {
        System.out.print(prompt);
        try {
            double value = Double.parseDouble(scanner.nextLine().trim());

            if (!Double.isFinite(value) || value <= 0) {
                return -1;
            }

            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
