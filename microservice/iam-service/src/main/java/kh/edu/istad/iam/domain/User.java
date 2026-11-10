package kh.edu.istad.iam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "username", unique = true, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "given_name", length = 100)
    private String givenName;

    @Column(name = "family_name", length = 100)
    private String familyName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "profile_url", length = 500)
    private String profileUrl;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "zoneinfo", length = 500)
    private String zoneinfo;

    @Column(name = "locale", length = 100)
    private String locale;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "phone_number_verified")
    private Boolean phoneNumberVerified;

    @Column(name = "address_street_address", length = 255)
    private String addressStreetAddress;

    @Column(name = "address_locality", length = 100)
    private String addressLocality;

    @Column(name = "address_region", length = 100)
    private String addressRegion;

    @Column(name = "address_postal_code", length = 20)
    private String addressPostalCode;

    @Column(name = "address_country", length = 2, columnDefinition = "CHAR(2)")
    private String addressCountry;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @Column(name = "password_changed_at")
    private OffsetDateTime passwordChangedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Version
    @Column(name = "version")
    private Long version;
}
