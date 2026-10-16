package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        @NotNull(message = "productId is required")
        UUID productId,
        @NotBlank(message = "productName is required")
        String productName,
        @NotNull(message = "quantity is required")
        @Positive(message = "quantity must be greater than zero")
        Integer quantity,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        BigDecimal price,
        @NotNull(message = "subTotal is required")
        @Positive(message = "subTotal must be greater than zero")
        BigDecimal subTotal
) {
}
