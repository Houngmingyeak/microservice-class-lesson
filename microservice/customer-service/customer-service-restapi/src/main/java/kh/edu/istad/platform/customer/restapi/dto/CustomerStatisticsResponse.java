package kh.edu.istad.platform.customer.restapi.dto;

import java.util.List;
import java.util.Map;

public record CustomerStatisticsResponse(
        long totalCustomers,
        Map<String, Long> byStatus,
        List<SegmentCountResponse> bySegment
) {
    public record SegmentCountResponse(String segmentId, String segmentName, long total) {}
}