package kh.edu.istad.iam.feature.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record GrantPermissionsToUserRequest(
        @NotEmpty(message = "At least one permission UUID is required")
        List<UUID> permissionUuids,

        @NotBlank(message = "Effect (GRANT or DENY) is required")
        String effect,

        OffsetDateTime expiresAt,
        String reason
) {
}

