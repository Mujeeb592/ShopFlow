
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyHandlingTest {

    public static void main(String[] args) {

        // Demonstration: double has a floating-point precision problem
        double floatingPointTotal = 0.0;

        for (int i = 0; i < 3; i++) {
            floatingPointTotal += 0.1;
        }

        System.out.println("Floating-point problem demonstration");
        System.out.println("Adding 0.1 three times:");
        System.out.println("Actual output: " + floatingPointTotal);

        // Test 1: Add Rs. 0.10 ten times using BigDecimal
        BigDecimal total = new BigDecimal("0.00");

        for (int i = 0; i < 10; i++) {
            total = total.add(new BigDecimal("0.10"));
        }

        total = total.setScale(2, RoundingMode.HALF_UP);

        System.out.println("\nTest 1: Add 0.10 ten times");
        System.out.println("Expected: 1.00");
        System.out.println("Actual:   " + total.toPlainString());
        printResult(total.compareTo(new BigDecimal("1.00")) == 0);

        // Test 2: HALF_UP rounding
        BigDecimal roundedPrice = new BigDecimal("0.465")
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println("\nTest 2: HALF_UP rounding");
        System.out.println("Expected: 0.47");
        System.out.println("Actual:   " + roundedPrice.toPlainString());
        printResult(roundedPrice.compareTo(new BigDecimal("0.47")) == 0);

        // Test 3: Ten percent discount on Rs. 1199
        Order percentageOrder = createOrder(
                1, "1199.00", 1
        );

        Discount percentageDiscount =
                new PercentageDiscount(new BigDecimal("10"));

        percentageOrder.applyDiscount(percentageDiscount);

        System.out.println("\nTest 3: Percentage Discount");
        System.out.println("Subtotal:    Rs. "
                + percentageOrder.getTotal().toPlainString());
        System.out.println("Discount:    Rs. "
                + percentageOrder.getDiscountAmount().toPlainString());
        System.out.println("Final total: Rs. "
                + percentageOrder.getFinalTotal().toPlainString());

        printResult(
                percentageOrder.getTotal()
                        .compareTo(new BigDecimal("1199.00")) == 0
                && percentageOrder.getDiscountAmount()
                        .compareTo(new BigDecimal("119.90")) == 0
                && percentageOrder.getFinalTotal()
                        .compareTo(new BigDecimal("1079.10")) == 0
        );

        // Test 4: Flat discount
        Order flatOrder = createOrder(
                2, "1199.00", 1
        );

        Discount flatDiscount =
                new FlatDiscount(new BigDecimal("100.00"));

        flatOrder.applyDiscount(flatDiscount);

        System.out.println("\nTest 4: Flat Discount");
        System.out.println("Subtotal:    Rs. "
                + flatOrder.getTotal().toPlainString());
        System.out.println("Discount:    Rs. "
                + flatOrder.getDiscountAmount().toPlainString());
        System.out.println("Final total: Rs. "
                + flatOrder.getFinalTotal().toPlainString());

        printResult(
                flatOrder.getDiscountAmount()
                        .compareTo(new BigDecimal("100.00")) == 0
                && flatOrder.getFinalTotal()
                        .compareTo(new BigDecimal("1099.00")) == 0
        );

        // Test 5: Discount cannot reduce final total below zero
        Order cappedOrder = createOrder(
                3, "1199.00", 1
        );

        Discount oversizedFlatDiscount =
                new FlatDiscount(new BigDecimal("1500.00"));

        cappedOrder.applyDiscount(oversizedFlatDiscount);

        System.out.println("\nTest 5: Flat Discount Cannot Exceed Subtotal");
        System.out.println("Subtotal:    Rs. "
                + cappedOrder.getTotal().toPlainString());
        System.out.println("Discount:    Rs. "
                + cappedOrder.getDiscountAmount().toPlainString());
        System.out.println("Final total: Rs. "
                + cappedOrder.getFinalTotal().toPlainString());

        printResult(
                cappedOrder.getDiscountAmount()
                        .compareTo(new BigDecimal("1199.00")) == 0
                && cappedOrder.getFinalTotal()
                        .compareTo(new BigDecimal("0.00")) == 0
        );

        // Test 6: Buy 3 Get 1 Free with different prices
        Customer customer = createCustomer();

        Order buyThreeGetOneFreeOrder = new Order(4, customer);

        Product product1 = new Product(
                "T001", "Product A", "Testing",
                new BigDecimal("100.00"), 10
        );

        Product product2 = new Product(
                "T002", "Product B", "Testing",
                new BigDecimal("80.00"), 10
        );

        Product product3 = new Product(
                "T003", "Product C", "Testing",
                new BigDecimal("60.00"), 10
        );

        Product product4 = new Product(
                "T004", "Product D", "Testing",
                new BigDecimal("40.00"), 10
        );

        buyThreeGetOneFreeOrder.addItem(new OrderItem(product1, 1));
        buyThreeGetOneFreeOrder.addItem(new OrderItem(product2, 1));
        buyThreeGetOneFreeOrder.addItem(new OrderItem(product3, 1));
        buyThreeGetOneFreeOrder.addItem(new OrderItem(product4, 1));

        Discount buyThreeGetOneFree =
                new BuyThreeGetOneFreeDiscount();

        buyThreeGetOneFreeOrder.applyDiscount(buyThreeGetOneFree);

        System.out.println("\nTest 6: Buy 3 Get 1 Free");
        System.out.println("Subtotal:    Rs. "
                + buyThreeGetOneFreeOrder.getTotal().toPlainString());
        System.out.println("Discount:    Rs. "
                + buyThreeGetOneFreeOrder.getDiscountAmount().toPlainString());
        System.out.println("Final total: Rs. "
                + buyThreeGetOneFreeOrder.getFinalTotal().toPlainString());

        printResult(
                buyThreeGetOneFreeOrder.getTotal()
                        .compareTo(new BigDecimal("280.00")) == 0
                && buyThreeGetOneFreeOrder.getDiscountAmount()
                        .compareTo(new BigDecimal("40.00")) == 0
                && buyThreeGetOneFreeOrder.getFinalTotal()
                        .compareTo(new BigDecimal("240.00")) == 0
        );

        // Test 7: Fewer than four items means no free item
        Order threeItemsOrder = new Order(5, customer);

        Product item = new Product(
                "T005", "Sample Item", "Testing",
                new BigDecimal("100.00"), 10
        );

        threeItemsOrder.addItem(new OrderItem(item, 3));
        threeItemsOrder.applyDiscount(new BuyThreeGetOneFreeDiscount());

        System.out.println("\nTest 7: Buy 3 Get 1 Free with Only 3 Items");
        System.out.println("Subtotal:    Rs. "
                + threeItemsOrder.getTotal().toPlainString());
        System.out.println("Discount:    Rs. "
                + threeItemsOrder.getDiscountAmount().toPlainString());
        System.out.println("Final total: Rs. "
                + threeItemsOrder.getFinalTotal().toPlainString());

        printResult(
                threeItemsOrder.getTotal()
                        .compareTo(new BigDecimal("300.00")) == 0
                && threeItemsOrder.getDiscountAmount()
                        .compareTo(new BigDecimal("0.00")) == 0
                && threeItemsOrder.getFinalTotal()
                        .compareTo(new BigDecimal("300.00")) == 0
        );
    }

    private static Order createOrder(
            int orderId, String price, int quantity) {

        Customer customer = createCustomer();

        Product product = new Product(
                "P" + orderId,
                "Test Product",
                "Testing",
                new BigDecimal(price),
                10
        );

        Order order = new Order(orderId, customer);
        order.addItem(new OrderItem(product, quantity));

        return order;
    }

    private static Customer createCustomer() {
        return new Customer(
                1,
                "Mujeeb",
                "mujeeb@example.com",
                "03001234567"
        );
    }

    private static void printResult(boolean passed) {
        System.out.println("Passed:   " + passed);
    }
}
