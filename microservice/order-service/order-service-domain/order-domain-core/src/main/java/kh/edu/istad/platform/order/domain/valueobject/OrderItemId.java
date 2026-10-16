package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record OrderItemId(UUID id) {
    public OrderItemId {
        if (id == null) {
            throw new OrderDomainException("OrderItemId must not be null");
        }
    }
}
