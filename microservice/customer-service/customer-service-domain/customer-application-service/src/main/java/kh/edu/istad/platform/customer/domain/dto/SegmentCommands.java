package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.SegmentCriteria;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;

public final class SegmentCommands {
    private SegmentCommands() {}

    public record CreateSegmentCommand(String code, String name, String description,
                                       SegmentType type, SegmentCriteria criteria) {}

    public record UpdateSegmentCommand(String segmentId, String name, String description,
                                       SegmentCriteria criteria) {}
}
