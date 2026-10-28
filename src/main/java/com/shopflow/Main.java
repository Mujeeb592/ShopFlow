
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final BigDecimal ZERO_MONEY = new BigDecimal("0.00");
    private static final RoundingMode ROUNDING_RULE = RoundingMode.HALF_UP;

    private static final List<Product> products = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    // HashMap provides average O(1) product-code lookup.
    // Keys are normalized to make lookups case-insensitive.
    private static final Map<String, Product> productsByCode = new HashMap<>();

    public static void main(String[] args) {

        products.add(new Product(
                "101", "Keyboard", "Accessories",
                new BigDecimal("1500.00"), 10));

        products.add(new Product(
                "102", "Mouse", "Accessories",
                new BigDecimal("800.00"), 15));

        products.add(new Product(
                "103", "Monitor", "Electronics",
                new BigDecimal("25000.00"), 5));

        rebuildProductIndex();

        boolean running = true;

        while (running) {
            System.out.println("\n========== SHOPFLOW MENU ==========");
            System.out.println("1. View Products");
            System.out.println("2. Add Product");
            System.out.println("3. Search Product by Code");
            System.out.println("4. Register Customer");
            System.out.println("5. Place Order");
            System.out.println("6. View Order / Receipt");
            System.out.println("7. Change Order Status");
            System.out.println("8. Apply Discount to Order");
            System.out.println("9. Find Duplicates");
            System.out.println("10. Search Products by Name");
            System.out.println("11. Filter Products by Category");
            System.out.println("12. Filter Products by Price Range");
            System.out.println("13. Show Low-Stock Products");
            System.out.println("14. Find Orders by Customer");
            System.out.println("15. Exit");
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
                    changeOrderStatus();
                    break;
                case "8":
                    applyDiscountToOrder();
                    break;
                case "9":
                    findDuplicates();
                    break;
                case "10":
                    searchProductsByName();
                    break;
                case "11":
                    filterProductsByCategory();
                    break;
                case "12":
                    filterProductsByPriceRange();
                    break;
                case "13":
                    showLowStockProducts();
                    break;
                case "14":
                    findOrdersByCustomer();
                    break;
                case "15":
                    running = false;
                    System.out.println("Thank you for using ShopFlow!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-15.");
            }
        }

        scanner.close();
    }

    // ---------------- PRODUCT INDEX ----------------

    private static String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private static void rebuildProductIndex() {
        productsByCode.clear();

        for (Product product : products) {
            productsByCode.put(normalizeCode(product.getCode()), product);
        }
    }

    private static Product findProduct(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }

        return productsByCode.get(normalizeCode(code));
    }

    // ---------------- VIEW PRODUCTS ----------------

    private static void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n---------------- PRODUCT LIST ----------------");
        printProductHeader();

        BigDecimal stockValue = ZERO_MONEY;

        for (Product product : products) {
            product.displayProduct();
            stockValue = stockValue.add(product.getStockValue());
        }

        System.out.println("----------------------------------------------");
        System.out.println("Total products: " + products.size());
        System.out.println("Total stock value: Rs. " + formatMoney(stockValue));
    }

    private static void printProductHeader() {
        System.out.printf("%-10s %-20s %-20s %10s %10s%n",
                "Code", "Name", "Category", "Price", "Stock");
    }

    private static void displayProductResults(
            List<Product> results, String emptyMessage) {

        if (results.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        System.out.println("\n---------------- SEARCH RESULTS ----------------");
        printProductHeader();

        for (Product product : results) {
            product.displayProduct();
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Results found: " + results.size());
    }

    // ---------------- ADD PRODUCT ----------------

    private static void addProduct() {
        System.out.println("\n========== ADD PRODUCT ==========");

        String code = readNonEmpty("Enter product code: ");

        if (!code.matches("[A-Za-z0-9-]+")) {
            System.out.println(
                    "Product code can contain letters, digits, and hyphens only."
            );
            return;
        }

        if (findProduct(code) != null) {
            System.out.println(
                    "Product rejected: code '" + code
                            + "' already exists. Product codes are case-insensitive."
            );
            return;
        }

        String name = readNonEmpty("Enter product name: ");
        String category = readNonEmpty("Enter category: ");

        BigDecimal price = readPositivePrice("Enter price: ");

        if (price == null) {
            System.out.println("Enter a valid price greater than zero.");
            return;
        }

        int quantity = readNonNegativeInt("Enter opening stock: ");

        if (quantity < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        try {
            Product product = new Product(
                    code, name, category, price, quantity
            );

            products.add(product);
            productsByCode.put(normalizeCode(product.getCode()), product);

            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Product could not be added: " + e.getMessage());
        }
    }

    // ---------------- SEARCH PRODUCT BY CODE ----------------

    private static void searchProduct() {
        String code = readNonEmpty("Enter product code to search: ");
        Product product = findProduct(code);

        if (product == null) {
            System.out.println("Product not found.");
        } else {
            printProductHeader();
            product.displayProduct();
        }
    }

    // ---------------- SEARCH BY PARTIAL NAME ----------------

    private static void searchProductsByName() {
        System.out.println("\n========== SEARCH PRODUCTS BY NAME ==========");

        String searchText = readNonEmpty(
                "Enter all or part of the product name: "
        ).toLowerCase(Locale.ROOT);

        List<Product> results = new ArrayList<>();

        for (Product product : products) {
            if (product.getName().toLowerCase(Locale.ROOT)
                    .contains(searchText)) {
                results.add(product);
            }
        }

        displayProductResults(results, "No products matched that name.");
    }

    // ---------------- FILTER BY CATEGORY ----------------

    private static void filterProductsByCategory() {
        System.out.println("\n========== FILTER BY CATEGORY ==========");

        String category = readNonEmpty(
                "Enter category: "
        ).toLowerCase(Locale.ROOT);

        List<Product> results = new ArrayList<>();

        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                results.add(product);
            }
        }

        displayProductResults(results, "No products found in that category.");
    }

    // ---------------- FILTER BY PRICE RANGE ----------------

    private static void filterProductsByPriceRange() {
        System.out.println("\n========== FILTER BY PRICE RANGE ==========");

        BigDecimal minimumPrice = readNonNegativePrice(
                "Enter minimum price: "
        );

        if (minimumPrice == null) {
            System.out.println("Invalid minimum price.");
            return;
        }

        BigDecimal maximumPrice = readNonNegativePrice(
                "Enter maximum price: "
        );

        if (maximumPrice == null) {
            System.out.println("Invalid maximum price.");
            return;
        }

        if (minimumPrice.compareTo(maximumPrice) > 0) {
            System.out.println(
                    "Minimum price cannot be greater than maximum price."
            );
            return;
        }

        List<Product> results = new ArrayList<>();

        for (Product product : products) {
            boolean aboveMinimum =
                    product.getPrice().compareTo(minimumPrice) >= 0;

            boolean belowMaximum =
                    product.getPrice().compareTo(maximumPrice) <= 0;

            if (aboveMinimum && belowMaximum) {
                results.add(product);
            }
        }

        displayProductResults(
                results,
                "No products found within that price range."
        );
    }

    // ---------------- LOW STOCK ----------------

    private static void showLowStockProducts() {
        System.out.println("\n========== LOW-STOCK PRODUCTS ==========");
        System.out.println("Low stock means quantity is below 5.");

        List<Product> results = new ArrayList<>();

        for (Product product : products) {
            if (product.getQuantity() < 5) {
                results.add(product);
            }
        }

        displayProductResults(results, "No low-stock products found.");
    }

    // ---------------- REGISTER CUSTOMER ----------------

    private static void registerCustomer() {
        System.out.println("\n========== REGISTER CUSTOMER ==========");

        String name = readNonEmpty("Enter customer name: ");
        String email = readNonEmpty("Enter email: ");
        String phone = readNonEmpty("Enter phone: ");

        int id = customers.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;

        try {
            Customer customer = new Customer(id, name, email, phone);

            if (customers.contains(customer)) {
                System.out.println(
                        "Customer rejected: customer ID already exists."
                );
                return;
            }

            customers.add(customer);

            System.out.println("\nCustomer registered successfully!");
            System.out.println(customer);
        } catch (IllegalArgumentException e) {
            System.out.println("Customer could not be registered: "
                    + e.getMessage());
        }
    }

    // ---------------- PLACE ORDER ----------------

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

        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            if (entry.getValue() > entry.getKey().getQuantity()) {
                System.out.println(
                        "Order refused: insufficient stock for "
                                + entry.getKey().getName()
                );
                return;
            }
        }

        int orderId = orders.stream()
                .mapToInt(Order::getId)
                .max()
                .orElse(0) + 1;

        Order order = new Order(orderId, customer);

        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            order.addItem(new OrderItem(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<Product, Integer> entry : requestedItems.entrySet()) {
            boolean reduced = entry.getKey().reduceStock(entry.getValue());

            if (!reduced) {
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

    // ---------------- VIEW ORDER / RECEIPT ----------------

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
                    + " | Subtotal: Rs. " + formatMoney(order.getTotal())
                    + " | Final Total: Rs. " + formatMoney(order.getFinalTotal())
                    + " | Status: " + order.getStatus());
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

    // ---------------- FIND ORDERS BY CUSTOMER ----------------

    private static void findOrdersByCustomer() {
        System.out.println("\n========== FIND ORDERS BY CUSTOMER ==========");

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println(
                    "Customer ID: " + customer.getId()
                            + " | Name: " + customer.getName()
            );
        }

        int customerId = readPositiveInt("Enter customer ID: ");

        if (customerId <= 0) {
            System.out.println("Invalid customer ID.");
            return;
        }

        Customer customer = findCustomer(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        List<Order> customerOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getCustomer().getId() == customerId) {
                customerOrders.add(order);
            }
        }

        if (customerOrders.isEmpty()) {
            System.out.println(
                    "No orders found for customer: " + customer.getName()
            );
            return;
        }

        System.out.println("\nOrders for " + customer.getName() + ":");

        for (Order order : customerOrders) {
            System.out.println(
                    "Order ID: " + order.getId()
                            + " | Date: " + order.getDate()
                            + " | Total: Rs. " + formatMoney(order.getTotal())
                            + " | Final Total: Rs. "
                            + formatMoney(order.getFinalTotal())
                            + " | Status: " + order.getStatus()
            );
        }

        System.out.println("Total orders found: " + customerOrders.size());
    }

    // ---------------- CHANGE ORDER STATUS ----------------

    private static void changeOrderStatus() {
        if (orders.isEmpty()) {
            System.out.println("No orders available to update.");
            return;
        }

        System.out.println("\n========== CHANGE ORDER STATUS ==========");

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId()
                    + " | Customer: " + order.getCustomer().getName()
                    + " | Current Status: " + order.getStatus());
        }

        int orderId = readPositiveInt("Enter order ID: ");

        if (orderId <= 0) {
            System.out.println("Invalid order ID.");
            return;
        }

        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Current status: " + order.getStatus());
        System.out.println("Available statuses:");

        for (OrderStatus status : order.getStatus().getAllowedNextStatuses()) {
            System.out.println("- " + status);
        }

        System.out.print("Enter new status: ");
        String input = scanner.nextLine().trim();

        OrderStatus newStatus;

        try {
            newStatus = OrderStatus.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid status. Please enter one of the listed statuses."
            );
            return;
        }

        try {
            order.changeStatus(newStatus);
            System.out.println(
                    "Order status updated successfully to "
                            + order.getStatus() + "."
            );
        } catch (IllegalStateException e) {
            System.out.println("Status change refused: " + e.getMessage());
        }
    }

    // ---------------- APPLY DISCOUNT ----------------

    private static void applyDiscountToOrder() {
        if (orders.isEmpty()) {
            System.out.println("No orders available to apply a discount.");
            return;
        }

        System.out.println("\n========== APPLY DISCOUNT ==========");

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId()
                    + " | Customer: " + order.getCustomer().getName()
                    + " | Subtotal: Rs. " + formatMoney(order.getTotal())
                    + " | Current Discount: "
                    + (order.getAppliedDiscount() == null
                    ? "None" : order.getAppliedDiscount().getName()));
        }

        int orderId = readPositiveInt("Enter order ID: ");

        if (orderId <= 0) {
            System.out.println("Invalid order ID.");
            return;
        }

        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("\nSelect discount type:");
        System.out.println("1. Percentage Discount");
        System.out.println("2. Flat Amount Discount");
        System.out.println("3. Buy 3 Get 1 Free");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();

        Discount discount;

        try {
            switch (choice) {
                case "1":
                    BigDecimal percentage = readPositivePrice(
                            "Enter percentage (greater than 0, up to 100): "
                    );

                    if (percentage == null
                            || percentage.compareTo(BigDecimal.ZERO) <= 0
                            || percentage.compareTo(new BigDecimal("100")) > 0) {
                        System.out.println(
                                "Percentage must be greater than 0 and at most 100."
                        );
                        return;
                    }

                    discount = new PercentageDiscount(percentage);
                    break;

                case "2":
                    BigDecimal amount = readPositivePrice(
                            "Enter flat discount amount in Rs.: "
                    );

                    if (amount == null
                            || amount.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println(
                                "Discount amount must be greater than zero."
                        );
                        return;
                    }

                    discount = new FlatDiscount(amount);
                    break;

                case "3":
                    discount = new BuyThreeGetOneFreeDiscount();
                    break;

                default:
                    System.out.println("Invalid discount choice.");
                    return;
            }

            order.applyDiscount(discount);

            System.out.println("\nDiscount applied successfully!");
            order.printReceipt();

        } catch (IllegalArgumentException e) {
            System.out.println("Could not apply discount: " + e.getMessage());
        }
    }

    // ---------------- FIND DUPLICATES ----------------

    private static void findDuplicates() {
        System.out.println("\n========== FIND DUPLICATES ==========");

        Set<Product> seenProducts = new HashSet<>();
        Set<Product> duplicateProducts = new LinkedHashSet<>();

        for (Product product : products) {
            if (!seenProducts.add(product)) {
                duplicateProducts.add(product);
            }
        }

        Set<Customer> seenCustomers = new HashSet<>();
        Set<Customer> duplicateCustomers = new LinkedHashSet<>();

        for (Customer customer : customers) {
            if (!seenCustomers.add(customer)) {
                duplicateCustomers.add(customer);
            }
        }

        if (duplicateProducts.isEmpty() && duplicateCustomers.isEmpty()) {
            System.out.println("No duplicate products or customers found.");
            return;
        }

        if (!duplicateProducts.isEmpty()) {
            System.out.println("\nDuplicate Products:");

            for (Product product : duplicateProducts) {
                System.out.println(
                        "Code: " + product.getCode()
                                + " | Name: " + product.getName()
                );
            }
        }

        if (!duplicateCustomers.isEmpty()) {
            System.out.println("\nDuplicate Customers:");

            for (Customer customer : duplicateCustomers) {
                System.out.println(
                        "ID: " + customer.getId()
                                + " | Name: " + customer.getName()
                );
            }
        }
    }

    // ---------------- FIND CUSTOMER / ORDER ----------------

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

    // ---------------- INPUT HELPERS ----------------

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

    private static BigDecimal readPositivePrice(String prompt) {
        System.out.print(prompt);

        try {
            BigDecimal value = new BigDecimal(scanner.nextLine().trim());

            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                return null;
            }

            return value.setScale(2, ROUNDING_RULE);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static BigDecimal readNonNegativePrice(String prompt) {
        System.out.print(prompt);

        try {
            BigDecimal value = new BigDecimal(scanner.nextLine().trim());

            if (value.compareTo(BigDecimal.ZERO) < 0) {
                return null;
            }

            return value.setScale(2, ROUNDING_RULE);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String formatMoney(BigDecimal amount) {
        return amount.setScale(2, ROUNDING_RULE).toPlainString();
    }
}
