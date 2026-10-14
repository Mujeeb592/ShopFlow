
package com.shopflow;

import java.math.BigDecimal;

public interface Discount {

    String getName();

    BigDecimal calculateDiscount(Order order);
}
