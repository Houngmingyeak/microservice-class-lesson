package kh.edu.istad.iam.feature.permission;

public record CreatePermissionCommand(
        String resource,
        String action,
        String description
) {
}

