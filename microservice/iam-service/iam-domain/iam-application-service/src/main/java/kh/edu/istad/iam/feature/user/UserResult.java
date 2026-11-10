package kh.edu.istad.iam.feature.user;

import java.time.LocalDate;
import java.util.UUID;

public record UserResult(
        UUID uuid,
        String username,
        String email,
        Boolean emailVerified,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        Boolean phoneNumberVerified,
        String pictureUrl,
        Boolean enabled
) {
}
