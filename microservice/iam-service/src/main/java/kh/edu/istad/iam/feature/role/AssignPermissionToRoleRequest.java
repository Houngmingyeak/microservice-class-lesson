package kh.edu.istad.iam.feature.role;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO to assign a permission (by UUID) to a role.
 */
public record AssignPermissionToRoleRequest(
        @NotBlank(message = "Permission UUID is required")
        String permissionUuid
) {
}
