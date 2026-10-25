// PaymentResult.java
package kh.edu.istad.platform.payment.domain.dto;

import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record PaymentResult(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
    public static PaymentResult from(Payment payment, List<String> failureMessages) {
        return new PaymentResult(
                payment.getId() == null ? null : payment.getId().value(),
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getPrice() == null ? null : payment.getPrice().amount(),
                payment.getPaymentStatus(),
                payment.getCreatedAt(),
                failureMessages
        );
    }
}