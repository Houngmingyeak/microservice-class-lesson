package kh.edu.istad.iam.feature.permission;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PermissionResult(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        Boolean isSystem,
        Boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

