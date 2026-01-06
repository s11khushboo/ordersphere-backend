package com.ordersphere.dto.order;

import com.ordersphere.entity.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(@NotNull(message = "Status is required")
                                       OrderStatus status) {
}
