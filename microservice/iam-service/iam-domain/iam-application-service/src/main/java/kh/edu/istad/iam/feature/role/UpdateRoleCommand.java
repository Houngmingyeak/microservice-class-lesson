package kh.edu.istad.iam.feature.role;

import java.util.List;
import java.util.UUID;

public record UpdateRoleCommand(
        String description,
        List<UUID> permissionUuids
) {
}

