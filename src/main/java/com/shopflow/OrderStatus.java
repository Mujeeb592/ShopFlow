
package com.shopflow;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    NEW,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public Set<OrderStatus> getAllowedNextStatuses() {
        switch (this) {
            case NEW:
                return EnumSet.of(PAID, CANCELLED);

            case PAID:
                return EnumSet.of(SHIPPED, CANCELLED);

            case SHIPPED:
                return EnumSet.of(DELIVERED);

            case DELIVERED:
            case CANCELLED:
                return EnumSet.noneOf(OrderStatus.class);

            default:
                throw new IllegalStateException(
                        "Unknown order status: " + this
                );
        }
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return nextStatus != null
                && getAllowedNextStatuses().contains(nextStatus);
    }

    public String getTransitionFailureReason(OrderStatus nextStatus) {
        if (nextStatus == null) {
            return "New status cannot be null.";
        }

        if (this == nextStatus) {
            return "Order is already " + this + ".";
        }

        if (this == DELIVERED) {
            return "DELIVERED is a final status. "
                    + "This order cannot be changed again.";
        }

        if (this == CANCELLED) {
            return "CANCELLED is a final status. "
                    + "This order cannot be changed again.";
        }

        return "Transition from " + this + " to " + nextStatus
                + " is not allowed. Allowed next statuses: "
                + getAllowedNextStatuses() + ".";
    }
}
