// GetPaymentUseCase.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.PaymentResult;

import java.util.UUID;

public interface GetPaymentUseCase {
    PaymentResult execute(UUID orderId);
}