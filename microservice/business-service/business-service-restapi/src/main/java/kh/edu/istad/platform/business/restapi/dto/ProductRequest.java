package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than zero")
        BigDecimal price,
        @NotNull(message = "quantity is required")
        @PositiveOrZero(message = "quantity must not be negative")
        Integer quantity,
        @NotNull(message = "available is required")
        Boolean available
) {
}
