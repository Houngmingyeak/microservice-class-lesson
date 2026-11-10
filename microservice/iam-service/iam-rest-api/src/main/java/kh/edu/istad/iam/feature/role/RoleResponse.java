package kh.edu.istad.iam.feature.role;

import java.util.UUID;

/**
 * Response DTO for Role.
 */
public record RoleResponse(
        UUID uuid,
        String name,
        String description,
        Boolean isSystem,
        Boolean isActive
) {
}
