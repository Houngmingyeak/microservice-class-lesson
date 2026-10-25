// GetPaymentUseCaseImpl.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.exception.PaymentNotFoundException;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class GetPaymentUseCaseImpl implements GetPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public GetPaymentUseCaseImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResult execute(UUID orderId) {
        return paymentRepository.findByOrderId(new OrderId(orderId))
                .map(p -> PaymentResult.from(p, List.of()))
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment for order " + orderId + " not found"));
    }
}