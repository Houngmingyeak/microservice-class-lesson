package kh.edu.istad.platform.order.domain.valueobject;

import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.UUID;

public record BusinessId(UUID id) {
    public BusinessId {
        if (id == null) {
            throw new OrderDomainException("BusinessId must not be null");
        }
    }
}
