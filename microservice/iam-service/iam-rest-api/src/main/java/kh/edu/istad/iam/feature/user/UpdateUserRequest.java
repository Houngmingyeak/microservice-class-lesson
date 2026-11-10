package kh.edu.istad.iam.feature.user;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for updating a User profile.
 */
public record UpdateUserRequest(
        @Size(max = 100)
        String givenName,

        @Size(max = 100)
        String familyName,

        @Size(max = 100)
        String middleName,

        @Size(max = 100)
        String nickname,

        @Size(max = 20)
        String gender,

        LocalDate birthdate,

        @Size(max = 20)
        String phoneNumber,

        @Size(max = 500)
        String pictureUrl,

        @Size(max = 500)
        String profileUrl,

        @Size(max = 500)
        String websiteUrl,

        @Size(max = 500)
        String zoneinfo,

        @Size(max = 100)
        String locale
) {
}
