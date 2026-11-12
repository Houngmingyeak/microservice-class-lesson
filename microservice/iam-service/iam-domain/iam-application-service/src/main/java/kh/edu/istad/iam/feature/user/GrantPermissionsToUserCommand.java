package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record GrantPermissionsToUserCommand(
        List<UUID> permissionUuids,
        String effect, // GRANT or DENY
        OffsetDateTime expiresAt,
        String reason
) {
}

