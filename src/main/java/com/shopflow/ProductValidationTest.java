
package com.shopflow;

import java.math.BigDecimal;

public class ProductValidationTest {

    public static void main(String[] args) {

        System.out.println("========== PRODUCT VALIDATION TESTS ==========");

        // Test 1: Valid product
        Product validProduct = new Product(
                " KB-200 ",
                " Wireless Keyboard ",
                " Accessories ",
                new BigDecimal("1500.00"),
                10
        );

        boolean validPassed =
                validProduct.getCode().equals("KB-200")
                && validProduct.getName().equals("Wireless Keyboard")
                && validProduct.getCategory().equals("Accessories");

        printResult("Valid product and trimming", validPassed);

        // Test 2: Empty product name
        expectInvalidProduct(
                "Empty product name",
                "KB-201", " ", "Accessories",
                new BigDecimal("100.00"), 5
        );

        // Test 3: Empty category
        expectInvalidProduct(
                "Empty product category",
                "KB-202", "Keyboard", "",
                new BigDecimal("100.00"), 5
        );

        // Test 4: Null price
        expectInvalidProduct(
                "Null price",
                "KB-203", "Keyboard", "Accessories",
                null, 5
        );

        // Test 5: Negative stock
        expectInvalidProduct(
                "Negative stock",
                "KB-204", "Keyboard", "Accessories",
                new BigDecimal("100.00"), -1
        );

        // Test 6: Invalid product code
        expectInvalidProduct(
                "Invalid product code",
                "KB 205", "Keyboard", "Accessories",
                new BigDecimal("100.00"), 5
        );

        // Test 7: Price that rounds to zero
        expectInvalidProduct(
                "Price rounds to zero",
                "KB-206", "Keyboard", "Accessories",
                new BigDecimal("0.004"), 5
        );
    }

    private static void expectInvalidProduct(
            String testName,
            String code,
            String name,
            String category,
            BigDecimal price,
            int quantity) {

        try {
            new Product(code, name, category, price, quantity);
            printResult(testName, false);
        } catch (IllegalArgumentException e) {
            printResult(testName, true);
        }
    }

    private static void printResult(String testName, boolean passed) {
        System.out.println(testName + ": " + passed);
    }
}
