package kh.edu.istad.platform.customer.domain.event;

import java.time.Instant;

public record SegmentCreatedEvent(String segmentId, String code, Instant occurredAt) {
    public static SegmentCreatedEvent of(String segmentId, String code) {
        return new SegmentCreatedEvent(segmentId, code, Instant.now());
    }
}
