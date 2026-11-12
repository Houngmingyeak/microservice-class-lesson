package kh.edu.istad.iam.feature.identity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IdentityResult(
        UUID uuid,
        String provider,
        String email,
        String displayName,
        OffsetDateTime linkedAt
) {
}

