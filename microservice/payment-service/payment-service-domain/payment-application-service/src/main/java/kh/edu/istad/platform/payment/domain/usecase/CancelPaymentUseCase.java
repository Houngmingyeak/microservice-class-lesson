// CancelPaymentUseCase.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;

public interface CancelPaymentUseCase {
    PaymentResult execute(CancelPaymentCommand command);
}