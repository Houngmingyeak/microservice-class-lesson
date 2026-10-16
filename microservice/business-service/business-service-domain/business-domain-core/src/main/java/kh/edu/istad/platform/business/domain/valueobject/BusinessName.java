package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

public record BusinessName(String legalName, String displayName) {
    public BusinessName {
        if (legalName == null || legalName.isBlank()) {
            throw new BusinessDomainException("Legal name must not be blank");
        }
        if (displayName == null || displayName.isBlank()) {
            throw new BusinessDomainException("Display name must not be blank");
        }
        if (legalName.length() > 100) {
            throw new BusinessDomainException("Legal name must not be longer than 100 characters");
        }
        if (displayName.length() > 100) {
            throw new BusinessDomainException("Display name must not be longer than 100 characters");
        }
    }
}
