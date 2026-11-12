package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PermissionGrantResult(
        UUID permissionUuid,
        String permissionName,
        String effect,
        OffsetDateTime expiresAt,
        String reason
) {
}

