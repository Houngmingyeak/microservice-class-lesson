package kh.edu.istad.platform.customer.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document("customer_segments")
public record CustomerSegmentDocument(
        @Id String id,
        @Indexed(unique = true) String code,
        @Indexed(unique = true) String nameLower,
        String name,
        String description,
        String type,
        CriteriaDocument criteria,
        @Indexed String status,
        Instant createdAt,
        Instant updatedAt) {

    public record CriteriaDocument(BigDecimal minTotalSpend, Integer minOrderCount, String country) {}
}
