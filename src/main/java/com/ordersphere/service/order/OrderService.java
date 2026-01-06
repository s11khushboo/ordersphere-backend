package com.ordersphere.service.order;

import com.ordersphere.dto.order.CreateOrderRequest;
import com.ordersphere.dto.order.OrderResponse;
import com.ordersphere.entity.order.Order;
import com.ordersphere.entity.order.OrderItem;
import com.ordersphere.entity.order.OrderStatus;
import com.ordersphere.exception.OrderNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.ordersphere.repository.order.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setUserId(request.userId());

        List<OrderItem> items = request.items().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(item.productId());
                    orderItem.setQuantity(item.quantity());
                    orderItem.setPrice(item.price());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();

        order.setItems(items);

        BigDecimal totalAmount = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getStatus(),
                saved.getTotalAmount()
        );
    }
    public List<OrderResponse> getOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getStatus(),
                        order.getTotalAmount()
                )).toList();
    }
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }
    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus currentStatus = order.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return new OrderResponse(
                updatedOrder.getId(),
                updatedOrder.getStatus(),
                updatedOrder.getTotalAmount()
        );
    }

    private void validateStatusTransition(
            OrderStatus current,
            OrderStatus next) {

        if (current == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order cannot be updated");
        }

        if (current == OrderStatus.CREATED && next != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be PAID before further processing");
        }

        if (current == OrderStatus.PAID &&
                !(next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Paid order can only be SHIPPED or CANCELLED");
        }

        if (current == OrderStatus.SHIPPED && next != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Shipped order can only be DELIVERED");
        }
    }






}
