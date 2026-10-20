package kh.edu.istad.platform.customer.domain.event;

import java.time.Instant;

public record SegmentUpdatedEvent(String segmentId, String code, Instant occurredAt) {
    public static SegmentUpdatedEvent of(String segmentId, String code) {
        return new SegmentUpdatedEvent(segmentId, code, Instant.now());
    }
}
