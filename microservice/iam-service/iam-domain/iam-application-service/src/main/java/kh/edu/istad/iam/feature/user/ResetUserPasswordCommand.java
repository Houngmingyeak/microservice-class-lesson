package kh.edu.istad.iam.feature.user;

import java.util.UUID;

public record ResetUserPasswordCommand(
        UUID userUuid,
        String newPassword
) {
}

