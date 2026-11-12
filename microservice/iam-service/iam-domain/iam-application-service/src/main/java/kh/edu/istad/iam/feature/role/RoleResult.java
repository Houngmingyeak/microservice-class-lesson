package kh.edu.istad.iam.feature.role;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RoleResult(
        UUID uuid,
        String name,
        String description,
        Boolean isSystem,
        Boolean isActive,
        List<String> permissions,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

