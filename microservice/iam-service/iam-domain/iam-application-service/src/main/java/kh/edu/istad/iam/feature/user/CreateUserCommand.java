package kh.edu.istad.iam.feature.user;

import java.time.LocalDate;

public record CreateUserCommand(
        String username,
        String email,
        String password,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber
) {
}
