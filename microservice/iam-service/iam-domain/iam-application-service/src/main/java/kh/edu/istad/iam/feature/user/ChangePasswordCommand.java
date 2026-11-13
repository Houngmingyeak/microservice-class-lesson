package kh.edu.istad.iam.feature.user;

import java.util.UUID;

public record ChangePasswordCommand(
        UUID userUuid,
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}

