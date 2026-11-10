package kh.edu.istad.iam.feature.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a Permission.
 */
public record CreatePermissionRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Size(max = 50)
        String resource,

        @Size(max = 50)
        String action,

        @Size(max = 255)
        String description
) {
}
