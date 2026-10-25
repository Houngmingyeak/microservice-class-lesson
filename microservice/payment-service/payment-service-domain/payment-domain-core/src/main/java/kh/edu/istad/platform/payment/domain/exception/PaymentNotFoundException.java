package kh.edu.istad.platform.payment.domain.exception;

public class PaymentNotFoundException extends PaymentDomainException {

    public PaymentNotFoundException(String message) {
        super(message);
    }
}