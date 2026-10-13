
package com.shopflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyThreeGetOneFreeDiscount implements Discount {

    @Override
    public String getName() {
        return "Buy 3 Get 1 Free";
    }

    @Override
    public double calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        List<Double> unitPrices = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                unitPrices.add(item.getPriceAtPurchase());
            }
        }

        if (unitPrices.size() < 4) {
            return 0;
        }

        unitPrices.sort(Collections.reverseOrder());

        double discount = 0;

        for (int i = 3; i < unitPrices.size(); i += 4) {
            discount += unitPrices.get(i);
        }

        return Math.min(discount, order.getTotal());
    }
}
