package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

import java.util.UUID;

public record BusinessId(UUID value) {
    public BusinessId {
        if (value == null) {
            throw new BusinessDomainException("BusinessId must not be null");
        }
    }
}
