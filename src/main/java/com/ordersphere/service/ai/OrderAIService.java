package com.ordersphere.service.ai;

import com.ordersphere.client.ai.OpenAIClient;
import com.ordersphere.entity.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderAIService {

    private final OpenAIClient openAIClient;

    public OrderAIService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String generateSummary(List<com.ordersphere.entity.order.Order> orders, String question) {

        String orderContext = orders.stream()
                .map(order -> "OrderId=" + order.getId()
                        + ", Status=" + order.getStatus()
                        + ", Amount=" + order.getTotalAmount())
                .collect(Collectors.joining("\n"));

        String prompt = """
                You are an order management assistant.
                Below are the orders:

                %s

                Question:
                %s
                """.formatted(orderContext, question);

        return openAIClient.ask(prompt);
    }
}
