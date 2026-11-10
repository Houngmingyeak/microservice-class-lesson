package kh.edu.istad.iam.feature.userrole;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO to assign a role (by UUID) to a user.
 */
public record AssignRoleRequest(
        @NotBlank(message = "Role UUID is required")
        String roleUuid
) {
}
