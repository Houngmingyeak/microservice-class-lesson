package kh.edu.istad.iam.feature.role;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO to assign permissions to a role.
 */
public record AssignPermissionToRoleRequest(
        @NotEmpty(message = "At least one permission UUID is required")
        List<UUID> permissionUuids
) {
}
