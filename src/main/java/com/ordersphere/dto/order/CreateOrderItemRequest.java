package com.ordersphere.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(
        UUID productId,
        int quantity,
        BigDecimal price
) {}

