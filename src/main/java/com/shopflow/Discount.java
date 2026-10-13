
package com.shopflow;

public interface Discount {

    String getName();

    double calculateDiscount(Order order);
}
