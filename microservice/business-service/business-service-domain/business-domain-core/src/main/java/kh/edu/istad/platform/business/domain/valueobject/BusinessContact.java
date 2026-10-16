package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

public record BusinessContact(String email, String phoneNumber) {
    public BusinessContact {
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BusinessDomainException("Business email is not valid");
        }
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[0-9 \\-]{8,20}$")) {
            throw new BusinessDomainException("Business phone number is not valid");
        }
    }
}
