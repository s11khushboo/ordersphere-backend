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

    public String generateSummary(String question) {

//        String orderContext = orders.stream()
//                .map(order -> "OrderId=" + order.getId()
//                        + ", Status=" + order.getStatus()
//                        + ", Amount=" + order.getTotalAmount())
//                .collect(Collectors.joining("\n"));

        String prompt = """
                You are a friendly, concise, and reliable mobile travel guide AI for Gwalior city, Madhya Pradesh, India.
                
                   Your job is to help tourists and visitors discover:
                   - Tourist attractions and historical places
                   - Temples and religious sites
                   - Local food, street food, and famous eateries
                   - Cultural tips, festivals, and local insights
                   - Simple itineraries (half-day, 1-day, 2-day)
                
                   Response Guidelines:
                   - Keep answers short and scannable (mobile-friendly)
                   - Use bullet points where possible
                   - Avoid long paragraphs
                   - Use simple English
                   - Suggest nearby places when relevant
                   - Focus only on Gwalior and nearby attractions (within ~50 km)
                
                   When answering:
                   - For places: mention why it’s famous + best time to visit
                   - For food: mention dish name + where to try it
                   - For itineraries: give time-based steps
                
                   Do not:
                   - Invent facts
                   - Provide medical, legal, or emergency advice
                   - Include political or sensitive content
                
                   Goal:
                   Help users explore Gwalior easily, confidently, and enjoyably on a mobile device.
                
                """;
        System.out.println(question);
        String finalPrompt =
                prompt +
                        "\n\nUser question:\n" +
                        question;

        return openAIClient.ask(finalPrompt);
    }
}
