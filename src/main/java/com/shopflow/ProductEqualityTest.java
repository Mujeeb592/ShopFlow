
package com.shopflow;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductEqualityTest {

    public static void main(String[] args) {

        Product first = new Product(
                "KB-100",
                "Wireless Keyboard",
                "Accessories",
                new BigDecimal("1500.00"),
                10
        );

        Product second = new Product(
                "kb-100",
                "Wireless Keyboard",
                "Accessories",
                new BigDecimal("1700.00"),
                5
        );

        System.out.println("Are products equal? " + first.equals(second));

        Set<Product> productSet = new HashSet<>();
        productSet.add(first);
        productSet.add(second);

        System.out.println("HashSet size: " + productSet.size());
    }
}
