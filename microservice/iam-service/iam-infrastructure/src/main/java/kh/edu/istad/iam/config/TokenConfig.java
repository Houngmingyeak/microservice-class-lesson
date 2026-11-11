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
                // Add OIDC standard claims to ID token
                context.getClaims().claims(claims -> {
                    claims.put("preferred_username", username);
                    userOpt.ifPresent(user -> {
                        claims.put("sub", user.getUuid().toString());
                        if (user.getEmail() != null) {
                            claims.put("email", user.getEmail());
                        }
                        claims.put("email_verified", Boolean.TRUE.equals(user.getEmailVerified()));
                        if (user.getGivenName() != null) {
                            claims.put("given_name", user.getGivenName());
                        }
                        if (user.getFamilyName() != null) {
                            claims.put("family_name", user.getFamilyName());
                        }
                        if (user.getPictureUrl() != null) {
                            claims.put("picture", user.getPictureUrl());
                        }
                        if (user.getLocale() != null) {
                            claims.put("locale", user.getLocale());
                        }
                        if (user.getZoneinfo() != null) {
                            claims.put("zoneinfo", user.getZoneinfo());
                        }
                    });
                });
            }
        };
    }
}
