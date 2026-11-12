package kh.edu.istad.iam.feature.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a Role.
 */
public record CreateRoleRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        List<UUID> permissionUuids
) {
}
