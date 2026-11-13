package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Plain Java base model that carries the audit fields.
 * No JPA or Spring code is allowed here.
 */
@Getter
@Setter
public abstract class AuditableModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
}
