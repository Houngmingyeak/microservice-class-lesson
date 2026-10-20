package kh.edu.istad.platform.customer.domain.event;

import java.time.Instant;

public record SegmentDeactivatedEvent(String segmentId, String code, Instant occurredAt) {
    public static SegmentDeactivatedEvent of(String segmentId, String code) {
        return new SegmentDeactivatedEvent(segmentId, code, Instant.now());
    }
}
