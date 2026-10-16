package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

import java.util.UUID;

public record OrderApprovalId(UUID value) {
    public OrderApprovalId {
        if (value == null) {
            throw new BusinessDomainException("OrderApprovalId must not be null");
        }
    }
}
