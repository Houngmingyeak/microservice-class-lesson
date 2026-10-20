package kh.edu.istad.platform.customer.domain.dto;

import java.util.List;
import java.util.Map;

public record CustomerStatistics(
        long totalCustomers,
        Map<String, Long> byStatus,
        List<SegmentCount> bySegment
) {
    public record SegmentCount(String segmentId, String segmentName, long total) {}
}