// PaymentRepository.java
package kh.edu.istad.platform.payment.domain.port.out;

import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;

import java.util.Optional;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findByOrderId(OrderId orderId);
}