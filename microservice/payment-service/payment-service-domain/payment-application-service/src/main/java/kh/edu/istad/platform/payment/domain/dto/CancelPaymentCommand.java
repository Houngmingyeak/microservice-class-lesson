package kh.edu.istad.platform.payment.domain.dto;

import java.util.UUID;

public record CancelPaymentCommand(UUID orderId) {
}