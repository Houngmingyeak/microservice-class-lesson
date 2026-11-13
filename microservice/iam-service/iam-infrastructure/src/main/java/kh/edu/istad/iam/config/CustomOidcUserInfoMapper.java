package kh.edu.istad.iam.config;

import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Maps User details to standard OIDC claims for the /userinfo endpoint (Section E.2).
 * Ensures 'sub' matches the UUID claim placed in the ID Token by TokenConfig.
 */
@Component
@RequiredArgsConstructor
public class CustomOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    private final UserJpaRepository userJpaRepository;

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        String username = context.getAuthorization().getPrincipalName();
        Optional<UserEntity> userOpt = userJpaRepository.findByUsernameOrEmail(username, username);

        Map<String, Object> claims = new HashMap<>();
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            claims.put("sub", user.getUuid().toString());
            claims.put("preferred_username", user.getUsername());
            String fullName = ((user.getGivenName() != null ? user.getGivenName() : "") + " "
                    + (user.getFamilyName() != null ? user.getFamilyName() : "")).trim();
            if (!fullName.isEmpty()) {
                claims.put("name", fullName);
            }
            if (user.getGivenName() != null) claims.put("given_name", user.getGivenName());
            if (user.getFamilyName() != null) claims.put("family_name", user.getFamilyName());
            if (user.getMiddleName() != null) claims.put("middle_name", user.getMiddleName());
            if (user.getNickname() != null) claims.put("nickname", user.getNickname());
            if (user.getEmail() != null) claims.put("email", user.getEmail());
            if (user.getEmailVerified() != null) claims.put("email_verified", user.getEmailVerified());
            if (user.getPhoneNumber() != null) claims.put("phone_number", user.getPhoneNumber());
            if (user.getPhoneNumberVerified() != null) claims.put("phone_number_verified", user.getPhoneNumberVerified());
            if (user.getPictureUrl() != null) claims.put("picture", user.getPictureUrl());
            if (user.getProfileUrl() != null) claims.put("profile", user.getProfileUrl());
            if (user.getWebsiteUrl() != null) claims.put("website", user.getWebsiteUrl());
            if (user.getGender() != null) claims.put("gender", user.getGender());
            if (user.getBirthdate() != null) claims.put("birthdate", user.getBirthdate().toString());
            if (user.getZoneinfo() != null) claims.put("zoneinfo", user.getZoneinfo());
            if (user.getLocale() != null) claims.put("locale", user.getLocale());
        } else {
            claims.put("sub", username);
            claims.put("preferred_username", username);
        }

        return new OidcUserInfo(claims);
    }
}

