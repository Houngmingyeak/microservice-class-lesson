package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RoleAssignmentResult(
        UUID roleUuid,
        String roleName,
        OffsetDateTime expiresAt,
        String reason
) {
}

