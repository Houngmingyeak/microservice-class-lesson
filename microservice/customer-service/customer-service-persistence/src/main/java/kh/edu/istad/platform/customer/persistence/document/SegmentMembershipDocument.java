package kh.edu.istad.platform.customer.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("segment_memberships")
@CompoundIndex(name = "segment_customer_uq", def = "{'segmentId': 1, 'customerId': 1}", unique = true)
public record SegmentMembershipDocument(@Id String id, String segmentId, String customerId, Instant joinedAt) {}
