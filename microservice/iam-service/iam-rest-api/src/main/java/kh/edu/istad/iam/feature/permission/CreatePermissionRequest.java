package kh.edu.istad.iam.feature.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a Permission.
 */
public record CreatePermissionRequest(
        @NotBlank(message = "Resource is required")
        @Size(max = 50, message = "Resource must not exceed 50 characters")
        String resource,

        @NotBlank(message = "Action is required")
        @Size(max = 50, message = "Action must not exceed 50 characters")
        String action,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
}
