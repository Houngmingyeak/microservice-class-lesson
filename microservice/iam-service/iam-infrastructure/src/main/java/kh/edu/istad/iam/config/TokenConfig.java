package kh.edu.istad.iam.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

/**
 * Customizes JWT access tokens and ID tokens to include
 * OIDC standard claims, roles, and permission authorities.
 */
@Configuration
@Slf4j
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication == null) {
                return;
            }

            var principal = authentication.getPrincipal();
            log.debug("Token customizer - principal type: {}", principal.getClass().getSimpleName());

            if (principal instanceof CustomUserDetails user) {
                String tokenType = context.getTokenType().getValue();

                if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType)) {
                    // Add roles and permissions to access token
                    context.getClaims().claims(claims -> {
                        claims.put("preferred_username", user.getUsername());
                        claims.put("sub", user.getUser().getUuid().toString());
                        claims.put("authorities", user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet()));
                        claims.put("roles", user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(a -> a.startsWith("ROLE_"))
                                .map(a -> a.substring(5))
                                .collect(Collectors.toSet()));
                    });
                }

                if (OidcParameterNames.ID_TOKEN.equals(tokenType)) {
                    // Add OIDC standard claims to ID token
                    context.getClaims().claims(claims -> {
                        claims.put("sub", user.getUser().getUuid().toString());
                        claims.put("preferred_username", user.getUsername());
                        claims.put("email", user.getUser().getEmail());
                        claims.put("email_verified", Boolean.TRUE.equals(user.getUser().getEmailVerified()));
                        claims.put("given_name", user.getUser().getGivenName());
                        claims.put("family_name", user.getUser().getFamilyName());
                        if (user.getUser().getPictureUrl() != null) {
                            claims.put("picture", user.getUser().getPictureUrl());
                        }
                        if (user.getUser().getLocale() != null) {
                            claims.put("locale", user.getUser().getLocale());
                        }
                        if (user.getUser().getZoneinfo() != null) {
                            claims.put("zoneinfo", user.getUser().getZoneinfo());
                        }
                    });
                }
            }
        };
    }
}
