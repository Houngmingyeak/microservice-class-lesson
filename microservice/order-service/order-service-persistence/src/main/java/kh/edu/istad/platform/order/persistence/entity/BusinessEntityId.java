package kh.edu.istad.platform.order.persistence.entity;

import java.util.UUID;

public record
BusinessEntityId(Integer businessId, UUID productId) {
}
