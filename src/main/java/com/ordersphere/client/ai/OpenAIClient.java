package com.ordersphere.client.ai;

import com.ordersphere.config.OpenAIProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIClient {
    private final RestClient restClient;
    private final OpenAIProperties properties;

    public OpenAIClient(OpenAIProperties properties, RestClient.Builder builder) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String ask(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "model", properties.getModel(),
                "messages", List.of(
                        Map.of("role", "system",
                                "content", "You are a helpful order management assistant."),
                        Map.of("role", "user",
                                "content", prompt)
                ),
                "temperature", 0.3
        );

        Map<String, Object> response = restClient.post()
                .uri("/chat/completions")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
        return extractAnswer(response);
    }

    @SuppressWarnings("unchecked")
    private String extractAnswer(Map<String, Object> response) {

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        Map<String, Object> message =
                (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString();
    }
}