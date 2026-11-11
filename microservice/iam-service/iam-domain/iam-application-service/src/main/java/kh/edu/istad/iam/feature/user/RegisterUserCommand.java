package kh.edu.istad.iam.feature.user;

import java.util.UUID;

public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String confirmPassword,
        String givenName,
        String familyName,
        String locale
) {
}

