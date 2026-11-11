package kh.edu.istad.iam.feature.user;

import java.time.LocalDate;

public record UpdateUserProfileCommand(
        String givenName,
        String familyName,
        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String pictureUrl,
        String profileUrl,
        String websiteUrl,
        String zoneinfo,
        String locale
) {
}

