package kh.edu.istad.iam.config;

import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Customizes JWT access tokens and ID tokens to include
 * OIDC standard claims, roles, and permission authorities.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class TokenConfig {

    private final UserJpaRepository userJpaRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication == null) {
                return;
            }

            String username = authentication.getName();
            Optional<UserEntity> userOpt = userJpaRepository.findByUsernameOrEmail(username, username);

            String tokenType = context.getTokenType().getValue();

            if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType)) {
                // Add roles and permissions to access token
                context.getClaims().claims(claims -> {
                    claims.put("preferred_username", username);
                    userOpt.ifPresent(u -> claims.put("sub", u.getUuid().toString()));
                    claims.put("authorities", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()));
                    claims.put("roles", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(a -> a.startsWith("ROLE_"))
                            .map(a -> a.substring(5))
                            .collect(Collectors.toSet()));
                });
            }

            if (OidcParameterNames.ID_TOKEN.equals(tokenType)) {
                // Add OIDC standard claims to ID token per Section E.2
                context.getClaims().claims(claims -> {
                    claims.put("preferred_username", username);
                    userOpt.ifPresent(user -> {
                        claims.put("sub", user.getUuid().toString());
                        String fullName = ((user.getGivenName() != null ? user.getGivenName() : "") + " "
                                + (user.getFamilyName() != null ? user.getFamilyName() : "")).trim();
                        if (!fullName.isEmpty()) {
                            claims.put("name", fullName);
                        }
                        if (user.getGivenName() != null) {
                            claims.put("given_name", user.getGivenName());
                        }
                        if (user.getFamilyName() != null) {
                            claims.put("family_name", user.getFamilyName());
                        }
                        if (user.getMiddleName() != null) {
                            claims.put("middle_name", user.getMiddleName());
                        }
                        if (user.getNickname() != null) {
                            claims.put("nickname", user.getNickname());
                        }
                        if (user.getGender() != null) {
                            claims.put("gender", user.getGender());
                        }
                        if (user.getBirthdate() != null) {
                            claims.put("birthdate", user.getBirthdate().toString());
                        }
                        if (user.getProfileUrl() != null) {
                            claims.put("profile", user.getProfileUrl());
                        }
                        if (user.getPictureUrl() != null) {
                            claims.put("picture", user.getPictureUrl());
                        }
                        if (user.getWebsiteUrl() != null) {
                            claims.put("website", user.getWebsiteUrl());
                        }
                        if (user.getZoneinfo() != null) {
                            claims.put("zoneinfo", user.getZoneinfo());
                        }
                        if (user.getLocale() != null) {
                            claims.put("locale", user.getLocale());
                        }
                        if (user.getUpdatedAt() != null) {
                            claims.put("updated_at", user.getUpdatedAt().toEpochSecond());
                        }

                        // email scope
                        if (user.getEmail() != null) {
                            claims.put("email", user.getEmail());
                        }
                        claims.put("email_verified", Boolean.TRUE.equals(user.getEmailVerified()));

                        // phone scope
                        if (user.getPhoneNumber() != null) {
                            claims.put("phone_number", user.getPhoneNumber());
                            claims.put("phone_number_verified", Boolean.TRUE.equals(user.getPhoneNumberVerified()));
                        }

                        // address scope
                        if (user.getAddressStreetAddress() != null || user.getAddressLocality() != null
                                || user.getAddressRegion() != null || user.getAddressPostalCode() != null
                                || user.getAddressCountry() != null) {
                            java.util.Map<String, Object> address = new java.util.HashMap<>();
                            if (user.getAddressStreetAddress() != null) address.put("street_address", user.getAddressStreetAddress());
                            if (user.getAddressLocality() != null) address.put("locality", user.getAddressLocality());
                            if (user.getAddressRegion() != null) address.put("region", user.getAddressRegion());
                            if (user.getAddressPostalCode() != null) address.put("postal_code", user.getAddressPostalCode());
                            if (user.getAddressCountry() != null) address.put("country", user.getAddressCountry());
                            claims.put("address", address);
                        }
                    });
                });
            }
        };
    }
}
