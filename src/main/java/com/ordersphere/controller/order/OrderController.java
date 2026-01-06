package com.ordersphere.controller.order;

import com.ordersphere.dto.order.CreateOrderRequest;
import com.ordersphere.dto.order.OrderResponse;
import com.ordersphere.dto.order.UpdateOrderStatusRequest;
import com.ordersphere.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
             @RequestBody CreateOrderRequest request) {

        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> fetchfOrder() {

        List<OrderResponse> orders = orderService.getOrders();
        return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }
    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequest request) {

        OrderResponse response =
                orderService.updateOrderStatus(orderId, request.status());

        return ResponseEntity.ok(response);
    }


}
