package kh.edu.istad.platform.payment.domain.valueobject;

import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;

import java.util.UUID;

public record CreditHistoryId(UUID value) {
    public CreditHistoryId {
        if (value == null) {
            throw new PaymentDomainException("CreditHistoryId must not be null");
        }
    }
}
