package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record OrderId(UUID id) {
    public OrderId {
        if (id == null) {
            throw new OrderDomainException("OrderId must not be null");
        }
    }
}
