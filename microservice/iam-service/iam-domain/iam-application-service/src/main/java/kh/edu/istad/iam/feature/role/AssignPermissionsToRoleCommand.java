package kh.edu.istad.iam.feature.role;

import java.util.List;
import java.util.UUID;

public record AssignPermissionsToRoleCommand(
        UUID roleUuid,
        List<UUID> permissionUuids
) {
}

