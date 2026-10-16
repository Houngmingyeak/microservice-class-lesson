package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "customerId is required")
        UUID customerId,
        @NotNull(message = "businessId is required")
        UUID businessId,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        BigDecimal price,
        @NotNull(message = "address is required")
        @Valid
        OrderAddressRequest address,
        @NotEmpty(message = "items must have at least one item")
        @Valid
        List<OrderItemRequest> items
) {
}
