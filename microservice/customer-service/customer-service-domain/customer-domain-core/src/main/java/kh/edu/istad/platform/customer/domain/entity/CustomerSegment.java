package kh.edu.istad.platform.customer.domain.entity;

import kh.edu.istad.platform.customer.domain.exception.CustomerSegmentDomainException;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentCriteria;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

import static kh.edu.istad.platform.customer.domain.exception.SegmentErrorCode.*;

public class CustomerSegment {

    private static final Pattern CODE = Pattern.compile("^[A-Z0-9_-]{2,30}$");

    private final String id;
    private final String code;
    private String name;
    private String description;
    private final SegmentType type;
    private SegmentCriteria criteria;
    private SegmentStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private CustomerSegment(String id, String code, String name, String description, SegmentType type,
                            SegmentCriteria criteria, SegmentStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id; this.code = code; this.name = name; this.description = description;
        this.type = type; this.criteria = criteria; this.status = status;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
    }

    /* ---------- factories ---------- */

    public static CustomerSegment create(String code, String name, String description,
                                         SegmentType type, SegmentCriteria criteria) {
        String normalizedCode = code == null ? null : code.trim().toUpperCase();
        if (normalizedCode == null || !CODE.matcher(normalizedCode).matches())
            throw invalid("Code must be 2-30 chars: A-Z, 0-9, '_' or '-'");
        if (type == null) throw invalid("Segment type is required");
        String n = validName(name);
        SegmentCriteria c = validCriteria(type, criteria);
        Instant now = Instant.now();
        return new CustomerSegment(UUID.randomUUID().toString(), normalizedCode, n, clean(description),
                type, c, SegmentStatus.ACTIVE, now, now);
    }

    public static CustomerSegment reconstitute(String id, String code, String name, String description,
                                               SegmentType type, SegmentCriteria criteria, SegmentStatus status,
                                               Instant createdAt, Instant updatedAt) {
        return new CustomerSegment(id, code, name, description, type, criteria, status, createdAt, updatedAt);
    }

    /* ---------- behaviour ---------- */

    /** Code and type are immutable after creation. */
    public void update(String name, String description, SegmentCriteria criteria) {
        if (status != SegmentStatus.ACTIVE)
            throw new CustomerSegmentDomainException(SEGMENT_INVALID_STATE, "Inactive segment cannot be updated; activate it first");
        this.name = validName(name);
        this.description = clean(description);
        this.criteria = validCriteria(type, criteria);
        touch();
    }

    public void activate() {
        if (status == SegmentStatus.ACTIVE)
            throw new CustomerSegmentDomainException(SEGMENT_INVALID_STATE, "Segment is already active");
        status = SegmentStatus.ACTIVE;
        touch();
    }

    public void deactivate() {
        if (status == SegmentStatus.INACTIVE)
            throw new CustomerSegmentDomainException(SEGMENT_INVALID_STATE, "Segment is already inactive");
        status = SegmentStatus.INACTIVE;
        touch();
    }

    /** Only ACTIVE + STATIC segments accept manual membership changes. */
    public void assertManualMembershipAllowed() {
        if (status != SegmentStatus.ACTIVE)
            throw new CustomerSegmentDomainException(SEGMENT_INVALID_STATE, "Segment is inactive");
        if (type != SegmentType.STATIC)
            throw new CustomerSegmentDomainException(SEGMENT_INVALID_STATE, "Members of a DYNAMIC segment are defined by criteria");
    }

    /* ---------- helpers ---------- */

    private static String validName(String name) {
        String n = name == null ? "" : name.trim();
        if (n.length() < 2 || n.length() > 100) throw invalid("Name must be 2-100 characters");
        return n;
    }

    private static SegmentCriteria validCriteria(SegmentType type, SegmentCriteria c) {
        boolean empty = c == null || c.isEmpty();
        if (type == SegmentType.DYNAMIC && empty) throw invalid("DYNAMIC segment requires at least one criteria rule");
        if (type == SegmentType.STATIC && !empty) throw invalid("STATIC segment must not define criteria");
        return empty ? null : c;
    }

    private static String clean(String s) { return s == null || s.isBlank() ? null : s.trim(); }
    private static CustomerSegmentDomainException invalid(String m) {
        return new CustomerSegmentDomainException(SEGMENT_INVALID_INPUT, m);
    }
    private void touch() { updatedAt = Instant.now(); }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public SegmentType getType() { return type; }
    public SegmentCriteria getCriteria() { return criteria; }
    public SegmentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
