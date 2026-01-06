package com.ordersphere.dto.order;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID userId,
        List<CreateOrderItemRequest> items
) {}

