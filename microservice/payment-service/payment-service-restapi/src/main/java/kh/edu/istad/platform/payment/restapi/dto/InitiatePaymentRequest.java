// InitiatePaymentRequest.java
package kh.edu.istad.platform.payment.restapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record InitiatePaymentRequest(
        @NotNull UUID orderId,
        @NotNull UUID customerId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price
) {
}