
package com.shopflow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

public class DuplicateDetectionTest {

    public static void main(String[] args) throws Exception {

        // Access the products list from Main.
        Field productsField = Main.class.getDeclaredField("products");
        productsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Product> products =
                (List<Product>) productsField.get(null);

        // Access the duplicate detection method from Main.
        Method findDuplicatesMethod =
                Main.class.getDeclaredMethod("findDuplicates");
        findDuplicatesMethod.setAccessible(true);

        // Create two products with the same code but different letter cases.
        Product firstProduct = new Product(
                "KB-100",
                "Keyboard",
                "Electronics",
                new BigDecimal("200.00"),
                10
        );

        Product duplicateProduct = new Product(
                "kb-100",
                "Wireless Keyboard",
                "Accessories",
                new BigDecimal("250.00"),
                5
        );

        try {
            // Add both products temporarily to test duplicate detection.
            products.add(firstProduct);
            products.add(duplicateProduct);

            System.out.println("========== DUPLICATE DETECTION TEST ==========");
            System.out.println("First product code: KB-100");
            System.out.println("Second product code: kb-100");
            System.out.println("Expected result: Duplicate product is reported.");

            // Run the existing duplicate detection method.
            findDuplicatesMethod.invoke(null);

        } finally {
            // Remove test products so the inventory stays unchanged.
            products.remove(firstProduct);
            products.remove(duplicateProduct);

            System.out.println("\nTemporary test products removed.");
        }
    }
}
