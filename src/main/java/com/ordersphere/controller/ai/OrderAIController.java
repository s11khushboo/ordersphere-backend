package com.ordersphere.controller.ai;

import com.ordersphere.dto.ai.AIQueryRequest;
import com.ordersphere.entity.order.Order;
import com.ordersphere.service.ai.OrderAIService;
import com.ordersphere.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/ai")
public class OrderAIController {

    private final OrderService orderService;
    private final OrderAIService orderAIService;

    public OrderAIController(OrderService orderService,
                             OrderAIService orderAIService) {
        this.orderService = orderService;
        this.orderAIService = orderAIService;
    }

    @PostMapping("/summary")
    public String summarizeOrders(@Valid @RequestBody AIQueryRequest request) {

        List<Order> orders = orderService.getAllOrdersInternal();
        return orderAIService.generateSummary(orders, request.question());
    }
}
