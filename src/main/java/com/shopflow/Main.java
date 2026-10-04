package com.shopflow;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();

        runMenu(scanner, products);

        scanner.close();
    }

    public static void runMenu(
            Scanner scanner,
            ArrayList<Product> products) {

        boolean running = true;

        while (running) {

            showMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    viewAllProducts(products);
                    break;

                case "2":
                    addProduct(scanner, products);
                    break;

                case "3":
                    searchProduct(scanner, products);
                    break;

                case "4":
                    exitMessage();
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1-4."
                    );
            }
        }
    }

    public static void showMenu() {

        System.out.println();
        System.out.println("===== ShopFlow Menu =====");
        System.out.println("1. View all products");
        System.out.println("2. Add product");
        System.out.println("3. Search by code");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void viewAllProducts(
            ArrayList<Product> products) {

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        printTableHeader();

        for (Product product : products) {
            product.displayProduct();
        }

        System.out.println();
        System.out.println(
                "Total products: " + products.size()
        );

        double totalStockValue = 0;

        for (Product product : products) {
            totalStockValue += product.getStockValue();
        }

        System.out.printf(
                "Total stock value: %.2f%n",
                totalStockValue
        );
    }

    public static void addProduct(
            Scanner scanner,
            ArrayList<Product> products) {

        Product product = createProduct(scanner, products);

        if (product != null) {

            products.add(product);

            System.out.println(
                    "Product added successfully."
            );
        }
    }

    public static Product createProduct(
            Scanner scanner,
            ArrayList<Product> products) {

        String code;

        while (true) {

            System.out.print("Enter product code: ");
            code = scanner.nextLine().trim();

            if (code.isEmpty()) {
                System.out.println(
                        "Code cannot be empty."
                );
                continue;
            }

            if (!code.matches("\\d+")) {
                System.out.println(
                        "Code must contain numbers only."
                );
                continue;
            }

            boolean duplicate = false;

            for (Product product : products) {

                if (product.getCode()
                        .equalsIgnoreCase(code)) {

                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {

                System.out.println(
                        "Product code already exists."
                );

            } else {

                break;
            }
        }

        String name;

        while (true) {

            System.out.print("Enter product name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {

                System.out.println(
                        "Name cannot be empty."
                );
                continue;
            }

            if (!name.matches("[a-zA-Z ]+")) {

                System.out.println(
                        "Name must contain letters and spaces only."
                );
                continue;
            }

            break;
        }

        String category;

        while (true) {

            System.out.print("Enter product category: ");
            category = scanner.nextLine().trim();

            if (category.isEmpty()) {

                System.out.println(
                        "Category cannot be empty."
                );
                continue;
            }

            if (!category.matches("[a-zA-Z ]+")) {

                System.out.println(
                        "Category must contain letters and spaces only."
                );
                continue;
            }

            break;
        }

        double price;

        while (true) {

            System.out.print("Enter product price: ");

            String priceInput =
                    scanner.nextLine().trim();

            try {

                price = Double.parseDouble(priceInput);

                if (price <= 0) {

                    System.out.println(
                            "Price must be greater than 0."
                    );
                    continue;
                }

                break;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid price. Please enter a number."
                );
            }
        }

        int quantity;

        while (true) {

            System.out.print("Enter stock quantity: ");

            String quantityInput =
                    scanner.nextLine().trim();

            try {

                quantity =
                        Integer.parseInt(quantityInput);

                if (quantity < 0) {

                    System.out.println(
                            "Stock quantity cannot be negative."
                    );
                    continue;
                }

                break;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid quantity. Please enter a whole number."
                );
            }
        }

        return new Product(
                code,
                name,
                category,
                price,
                quantity
        );
    }

    public static void searchProduct(
            Scanner scanner,
            ArrayList<Product> products) {

        System.out.print(
                "Enter product code to search: "
        );

        String code =
                scanner.nextLine().trim();

        for (Product product : products) {

            if (product.getCode()
                    .equalsIgnoreCase(code)) {

                System.out.println(
                        "Product found:"
                );

                printTableHeader();

                product.displayProduct();

                System.out.println();
                System.out.println(
                        "Debug information:"
                );

                System.out.println(product);

                return;
            }
        }

        System.out.println(
                "Product not found."
        );
    }

    public static void printTableHeader() {

        System.out.printf(
                "%-10s %-20s %-20s %10s %10s%n",
                "Code",
                "Name",
                "Category",
                "Price",
                "Stock"
        );

        System.out.println(
                "--------------------------------------------------------------------------"
        );
    }

    public static void exitMessage() {

        System.out.println(
                "Thank you for using ShopFlow."
        );
    }
}