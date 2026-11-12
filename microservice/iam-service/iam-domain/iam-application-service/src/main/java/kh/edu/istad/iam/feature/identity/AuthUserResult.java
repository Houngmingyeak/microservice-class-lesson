package kh.edu.istad.iam.feature.identity;

import java.util.Set;
import java.util.UUID;

public record AuthUserResult(
        UUID uuid,
        String username,
        String email,
        String passwordHash,
        Boolean enabled,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired,
        Set<String> authorities
) {
}

