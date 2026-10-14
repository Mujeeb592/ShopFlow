
package com.shopflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyThreeGetOneFreeDiscount implements Discount {

    @Override
    public String getName() {
        return "Buy 3 Get 1 Free";
    }

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null."
            );
        }

        List<BigDecimal> unitPrices = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                unitPrices.add(item.getPriceAtPurchase());
            }
        }

        if (unitPrices.size() < 4) {
            return BigDecimal.ZERO.setScale(2);
        }

        unitPrices.sort(Collections.reverseOrder());

        BigDecimal discount = BigDecimal.ZERO.setScale(2);

        for (int i = 3; i < unitPrices.size(); i += 4) {
            discount = discount.add(unitPrices.get(i));
        }

        return discount.min(order.getTotal())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
