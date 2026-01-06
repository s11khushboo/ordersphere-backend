package com.ordersphere.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record AIQueryRequest(@NotBlank(message = "Question is required")
                              String question) {
}
