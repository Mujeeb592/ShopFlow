package com.shopflow;

public class Main {

    public static void main(String[] args) {

        String[] codes = {
                "P001", "P002", "P003", "P004",
                "P005", "P006", "P007", "P008"
        };

        String[] names = {
                "Laptop", "Professional Wireless Mechanical Keyboard", "Office Chair", "Coffee Mug",
                "USB-C Cable", "Monitor", "Notebook", "Desk Lamp"
        };

        String[] categories = {
                "Electronics", "Electronics", "Furniture", "Kitchen",
                "Electronics", "Electronics", "Stationery", "Furniture"
        };

        double[] prices = {
                1200.00, 75.50, 250.00, 12.99,
                18.75, 450.00, 6.50, 85.00
        };

        int[] stock = {
                5, 12, 8, 30,
                25, 7, 40, 15
        };

        double totalStockValue = 0.0;

        System.out.println("SHOPFLOW PRODUCT CATALOGUE");
        System.out.println("--------------------------------------------------------------------------");

        System.out.printf("%-8s %-22s %-15s %10s %8s%n",
                "Code", "Name", "Category", "Price", "Stock");

        System.out.println("--------------------------------------------------------------------------");

        for (int i = 0; i < codes.length; i++) {

            System.out.printf("%-8s %-22s %-15s $%9.2f %8d%n",
                    codes[i],
                    names[i],
                    categories[i],
                    prices[i],
                    stock[i]);

            totalStockValue += prices[i] * stock[i];
        }

        System.out.println("--------------------------------------------------------------------------");

        System.out.printf("Products: %d%n", codes.length);

        System.out.printf("Total stock value: $%.2f%n", totalStockValue);
    }
}
