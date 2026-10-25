// PaymentWebMapper.java
package kh.edu.istad.platform.payment.restapi.mapper;

import kh.edu.istad.platform.payment.domain.dto.AddCreditCommand;
import kh.edu.istad.platform.payment.domain.dto.CreditResult;
import kh.edu.istad.platform.payment.domain.dto.InitiatePaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.restapi.dto.AddCreditRequest;
import kh.edu.istad.platform.payment.restapi.dto.CreditResponse;
import kh.edu.istad.platform.payment.restapi.dto.InitiatePaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {

    public InitiatePaymentCommand toCommand(InitiatePaymentRequest request) {
        return new InitiatePaymentCommand(request.orderId(), request.customerId(), request.price());
    }

    public AddCreditCommand toCommand(AddCreditRequest request) {
        return new AddCreditCommand(request.customerId(), request.amount());
    }

    public PaymentResponse toResponse(PaymentResult result) {
        return new PaymentResponse(
                result.paymentId(),
                result.orderId(),
                result.customerId(),
                result.price(),
                result.paymentStatus(),
                result.createdAt(),
                result.failureMessages());
    }

    public CreditResponse toResponse(CreditResult result) {
        return new CreditResponse(result.customerId(), result.totalCreditAmount());
    }
}