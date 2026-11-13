package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * User aggregate. Plain Java, no JPA, no Spring.
 */
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditableModel {

    private static final long serialVersionUID = 1L;

    private Long id;
    private UUID uuid;
    private String username;
    private String email;
    private Boolean emailVerified;
    private String passwordHash;
    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String gender;
    private LocalDate birthdate;
    private String profileUrl;
    private String pictureUrl;
    private String websiteUrl;
    private String zoneinfo;
    private String locale;
    private String phoneNumber;
    private Boolean phoneNumberVerified;
    private String addressStreetAddress;
    private String addressLocality;
    private String addressRegion;
    private String addressPostalCode;
    private String addressCountry;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Integer failedLoginAttempts;
    private OffsetDateTime lastLoginAt;
    private OffsetDateTime passwordChangedAt;
    private Boolean isDeleted;
    private OffsetDateTime deletedAt;
    private String deletedBy;
    private Long version;

    /**
     * Business rule: how a brand new user account starts its life.
     */
    public static User register(
            String username,
            String email,
            String passwordHash,
            String givenName,
            String familyName,
            String gender,
            LocalDate birthdate,
            String phoneNumber
    ) {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setGivenName(givenName);
        user.setFamilyName(familyName);
        user.setGender(gender);
        user.setBirthdate(birthdate);
        user.setPhoneNumber(phoneNumber);
        user.setEmailVerified(false);
        user.setPhoneNumberVerified(false);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setIsDeleted(false);
        user.setFailedLoginAttempts(0);
        return user;
    }
}
