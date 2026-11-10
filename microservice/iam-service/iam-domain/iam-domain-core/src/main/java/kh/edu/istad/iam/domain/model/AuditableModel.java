package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Plain Java base model that carries the audit fields.
 * No JPA or Spring code is allowed here.
 */
@Getter
@Setter
public abstract class AuditableModel {

    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
}
