package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record ProductId(UUID id) {
    public ProductId {
        if (id == null) {
            throw new OrderDomainException("ProductId must not be null");
        }
    }
}
