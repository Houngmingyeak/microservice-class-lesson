package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentCriteria;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;

import java.time.Instant;

public record SegmentResult(String id, String code, String name, String description, SegmentType type,
                            SegmentCriteria criteria, SegmentStatus status, long memberCount,
                            Instant createdAt, Instant updatedAt) {
    public static SegmentResult from(CustomerSegment s, long memberCount) {
        return new SegmentResult(s.getId(), s.getCode(), s.getName(), s.getDescription(), s.getType(),
                s.getCriteria(), s.getStatus(), memberCount, s.getCreatedAt(), s.getUpdatedAt());
    }
}
