package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UserAuthoritiesResult(
        List<RoleAuthority> roles,
        List<DirectPermission> directPermissions,
        List<String> effectivePermissions
) {
    public record RoleAuthority(
            UUID uuid,
            String name,
            OffsetDateTime expiresAt
    ) {
    }

    public record DirectPermission(
            String name,
            String effect
    ) {
    }
}

