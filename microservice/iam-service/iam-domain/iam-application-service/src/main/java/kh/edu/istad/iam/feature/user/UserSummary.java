package kh.edu.istad.iam.feature.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserSummary(
        UUID uuid,
        String username,
        String email,
        String name,
        String locale,
        Boolean enabled,
        OffsetDateTime createdAt
) {
}

