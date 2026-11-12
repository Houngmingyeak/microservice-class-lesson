package kh.edu.istad.iam.feature.role;

import java.util.UUID;

public record RoleSummary(
        UUID uuid,
        String name,
        Boolean active
) {
}

