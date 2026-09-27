package com.shopflow;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();
        boolean running = true;
        while (running) {
            System.out.println("\n===== ShopFlow =====");
            System.out.println("1. View all products");
            System.out.println("2. Add a new product");
            System.out.println("3. Search product by code");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
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
                    System.out.println("Thank you for using ShopFlow!");
                    break;
                default:
                    System.out.println(
                            "Invalid option. Please choose 1, 2, 3, or 4."
                    );
            }
        }
        scanner.close();
    }
    private static void viewAllProducts(ArrayList<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("\n===== All Products =====");
        for (Product product : products) {
            product.displayProduct();
        }
    }
    private static void addProduct(
            ArrayList<Product> products,
            Scanner scanner) {
        System.out.println("\n===== Add New Product =====");
        System.out.print("Enter product code: ");
        String code = scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter product quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        Product product = new Product(code, name, price, quantity);
        products.add(product);
        System.out.println("Product added successfully!");
    }
    private static void searchProduct(
            ArrayList<Product> products,
            Scanner scanner) {
        System.out.print("\nEnter product code to search: ");
        String code = scanner.nextLine();
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(code)) {
                System.out.println("\nProduct found:");
                product.displayProduct();
                return;
            }
        }
        System.out.println("No product found with code: " + code);
    }
}