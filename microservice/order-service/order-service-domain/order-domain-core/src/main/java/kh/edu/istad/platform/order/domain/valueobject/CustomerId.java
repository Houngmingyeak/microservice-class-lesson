package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record CustomerId(UUID id) {
    public CustomerId {
        if (id == null) {
            throw new OrderDomainException("CustomerId must not be null");
        }
    }
}
