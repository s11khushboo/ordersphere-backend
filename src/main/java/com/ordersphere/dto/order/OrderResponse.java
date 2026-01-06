package com.ordersphere.dto.order;

import com.ordersphere.entity.order.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        OrderStatus status,
        BigDecimal totalAmount
) {}