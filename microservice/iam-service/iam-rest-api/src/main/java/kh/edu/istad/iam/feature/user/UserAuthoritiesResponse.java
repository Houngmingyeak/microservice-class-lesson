package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UserAuthoritiesResponse(
        List<RoleAuthorityResponse> roles,
        List<DirectPermissionResponse> directPermissions,
        List<String> effectivePermissions
) {
    public record RoleAuthorityResponse(
            UUID uuid,
            String name,
            OffsetDateTime expiresAt
    ) {
    }

    public record DirectPermissionResponse(
            String name,
            String effect
    ) {
    }
}

