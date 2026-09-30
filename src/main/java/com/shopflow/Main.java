package com.shopflow;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();

        runMenu(products, scanner);

        scanner.close();
    }

    private static void runMenu(
            ArrayList<Product> products,
            Scanner scanner) {

        boolean running = true;

        while (running) {
            String choice = showMenu(scanner);

            switch (choice) {
                case "1":
                    viewAllProducts(products);
                    break;

                case "2":
                    addProduct(products, scanner);
                    break;

                case "3":
                    searchProduct(products, scanner);
                    break;

                case "4":
                    running = false;
                    exitMessage();
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1, 2, 3, or 4."
                    );
            }
        }
    }

    private static void viewAllProducts(ArrayList<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.println("\n===== All Products =====");
        printTableHeader();

        for (Product product : products) {
            printProduct(product);
        }
    }

    private static void addProduct(
            ArrayList<Product> products,
            Scanner scanner) {

        System.out.println("\n===== Add New Product =====");

        String code = readProductCode(products, scanner);
        String name = readNonEmptyName(scanner);
        double price = readValidPrice(scanner);
        int quantity = readValidQuantity(scanner);

        Product product = createProduct(code, name, price, quantity);

        products.add(product);

        System.out.println("Product added successfully!");
    }

    private static String readProductCode(
            ArrayList<Product> products,
            Scanner scanner) {

        while (true) {
            System.out.print("Enter product code: ");
            String code = scanner.nextLine().trim();

            if (code.isEmpty()) {
                System.out.println(
                        "Product code cannot be empty. Please enter a code."
                );
                continue;
            }

            if (isDuplicateCode(products, code)) {
                System.out.println(
                        "Product code already exists. Please enter a different code."
                );
                continue;
            }

            return code;
        }
    }

    private static boolean isDuplicateCode(
            ArrayList<Product> products,
            String code) {

        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }

        return false;
    }

    private static String readNonEmptyName(Scanner scanner) {

        while (true) {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println(
                        "Product name cannot be empty. Please enter a name."
                );
                continue;
            }

            return name;
        }
    }

    private static double readValidPrice(Scanner scanner) {

        while (true) {
            System.out.print("Enter product price: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(
                        "Price cannot be empty. Please enter a valid number."
                );
                continue;
            }

            try {
                double price = Double.parseDouble(input);

                if (price <= 0) {
                    System.out.println(
                            "Price must be greater than 0. Please enter a positive number."
                    );
                    continue;
                }

                return price;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid price. Please enter a valid number without commas."
                );
            }
        }
    }

    private static int readValidQuantity(Scanner scanner) {

        while (true) {
            System.out.print("Enter product quantity: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(
                        "Quantity cannot be empty. Please enter a valid whole number."
                );
                continue;
            }

            try {
                int quantity = Integer.parseInt(input);

                if (quantity < 0) {
                    System.out.println(
                            "Quantity cannot be negative. Please enter 0 or a positive whole number."
                    );
                    continue;
                }

                return quantity;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid quantity. Please enter a valid whole number."
                );
            }
        }
    }

    private static Product createProduct(
            String code,
            String name,
            double price,
            int quantity) {

        return new Product(code, name, price, quantity);
    }

    private static void searchProduct(
            ArrayList<Product> products,
            Scanner scanner) {

        System.out.print("\nEnter product code to search: ");
        String code = scanner.nextLine().trim();

        if (code.isEmpty()) {
            System.out.println(
                    "Product code cannot be empty. Please enter a code."
            );
            return;
        }

        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                System.out.println("\nProduct found:");
                printProduct(product);
                return;
            }
        }

        System.out.println("No product found with code: " + code);
    }

    private static String showMenu(Scanner scanner) {
        System.out.println("\n===== ShopFlow =====");
        System.out.println("1. View all products");
        System.out.println("2. Add a new product");
        System.out.println("3. Search product by code");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        return scanner.nextLine().trim();
    }

    private static void printProduct(Product product) {
        product.displayProduct();
    }

    private static void printTableHeader() {
        System.out.printf(
                "%-10s %-20s %10s %10s%n",
                "Code",
                "Name",
                "Price",
                "Quantity"
        );
    }

    private static void exitMessage() {
        System.out.println("Thank you for using ShopFlow!");
    }
}