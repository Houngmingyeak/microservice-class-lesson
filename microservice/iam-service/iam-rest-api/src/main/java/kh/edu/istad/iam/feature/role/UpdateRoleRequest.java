package kh.edu.istad.iam.feature.role;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating a Role.
 */
public record UpdateRoleRequest(
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        Boolean isActive
) {
}
