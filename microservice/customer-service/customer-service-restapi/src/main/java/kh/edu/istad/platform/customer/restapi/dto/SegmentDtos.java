package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;

import java.math.BigDecimal;
import java.time.Instant;

public final class SegmentDtos {
    private SegmentDtos() {}

    public record CriteriaDto(@PositiveOrZero BigDecimal minTotalSpend,
                              @PositiveOrZero Integer minOrderCount,
                              @Size(max = 2) String country) {}

    public record CreateSegmentRequest(
            @NotBlank @Pattern(regexp = "^[A-Za-z0-9_-]{2,30}$", message = "2-30 chars: letters, digits, _ or -") String code,
            @NotBlank @Size(min = 2, max = 100) String name,
            @Size(max = 500) String description,
            @NotNull SegmentType type,
            @Valid CriteriaDto criteria) {}

    public record UpdateSegmentRequest(
            @NotBlank @Size(min = 2, max = 100) String name,
            @Size(max = 500) String description,
            @Valid CriteriaDto criteria) {}

    public record SegmentResponse(String id, String code, String name, String description, SegmentType type,
                                  CriteriaDto criteria, SegmentStatus status, long memberCount,
                                  Instant createdAt, Instant updatedAt) {}
}
