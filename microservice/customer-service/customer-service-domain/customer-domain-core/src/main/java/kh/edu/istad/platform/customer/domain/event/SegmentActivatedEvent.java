package kh.edu.istad.platform.customer.domain.event;

import java.time.Instant;

public record SegmentActivatedEvent(String segmentId, String code, Instant occurredAt) {
    public static SegmentActivatedEvent of(String segmentId, String code) {
        return new SegmentActivatedEvent(segmentId, code, Instant.now());
    }
}
