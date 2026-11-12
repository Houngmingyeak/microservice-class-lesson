package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AssignRolesToUserCommand(
        List<UUID> roleUuids,
        OffsetDateTime expiresAt,
        String reason
) {
}

