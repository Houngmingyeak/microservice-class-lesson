package kh.edu.istad.platform.payment.domain.valueobject;

import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;

import java.util.UUID;

public record CreditEntryId(UUID value) {
    public CreditEntryId {
        if (value == null) {
            throw new PaymentDomainException("CreditEntryId must not be null");
        }
    }
}
