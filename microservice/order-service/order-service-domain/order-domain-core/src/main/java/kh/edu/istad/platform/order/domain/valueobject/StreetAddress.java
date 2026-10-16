package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record StreetAddress(UUID id, String street, String postalCode, String city) {
    public StreetAddress {
        if (street == null || street.isBlank()) {
            throw new OrderDomainException("Street must not be blank");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new OrderDomainException("Postal code must not be blank");
        }
        if (postalCode.length() > 10) {
            throw new OrderDomainException("Postal code must not be longer than 10 characters");
        }
        if (city == null || city.isBlank()) {
            throw new OrderDomainException("City must not be blank");
        }
    }
}
