package kh.edu.istad.iam.feature.permission;

import java.util.UUID;

/**
 * Response DTO for Permission.
 */
public record PermissionResponse(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        Boolean isSystem,
        Boolean isActive
) {
}
