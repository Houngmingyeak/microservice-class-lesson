// InitiatePaymentUseCase.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.InitiatePaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;

public interface InitiatePaymentUseCase {
    PaymentResult execute(InitiatePaymentCommand command);
}