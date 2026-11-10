package kh.edu.istad.iam.feature.permission;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating a Permission.
 */
public record UpdatePermissionRequest(
        @Size(max = 100)
        String name,

        @Size(max = 50)
        String resource,

        @Size(max = 50)
        String action,

        @Size(max = 255)
        String description,

        Boolean isActive
) {
}
