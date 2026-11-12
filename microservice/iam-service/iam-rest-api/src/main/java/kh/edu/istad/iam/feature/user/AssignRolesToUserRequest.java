package kh.edu.istad.iam.feature.user;

import jakarta.validation.constraints.NotEmpty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AssignRolesToUserRequest(
        @NotEmpty(message = "At least one role UUID is required")
        List<UUID> roleUuids,
        OffsetDateTime expiresAt,
        String reason
) {
}

