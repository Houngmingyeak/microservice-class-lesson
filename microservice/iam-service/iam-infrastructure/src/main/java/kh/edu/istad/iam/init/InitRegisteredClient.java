package kh.edu.istad.iam.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitRegisteredClient {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            // Clean up any stale authorizations/consents from previous runs
            jdbcTemplate.execute("DELETE FROM oauth2_authorization");
            jdbcTemplate.execute("DELETE FROM oauth2_authorization_consent");
            log.info("InitRegisteredClient: Cleared stale authorizations and consents.");
        } catch (Exception ex) {
            log.debug("InitRegisteredClient: Skipping cleanup: {}", ex.getMessage());
        }

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofHours(5))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .authorizationCodeTimeToLive(Duration.ofMinutes(3))
                .reuseRefreshTokens(false)
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(false)
                .build();

        // 1. Regular Client (front-bff-client) as Confidential Client for Gateway BFF
        var existingFront = registeredClientRepository.findByClientId("front-bff-client");
        RegisteredClient frontClient = RegisteredClient.withId(existingFront != null ? existingFront.getId() : UUID.randomUUID().toString())
                .clientId("front-bff-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:9999/login/oauth2/code/front-bff-client")
                .redirectUri("http://127.0.0.1:9999/login/oauth2/code/front-bff-client")
                .postLogoutRedirectUri("http://localhost:9999/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.ADDRESS)
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
        registeredClientRepository.save(frontClient);

        // 2. DCR Initial Access Token Client with scope client.create
        if (registeredClientRepository.findByClientId("dcr-admin-client") == null) {
            RegisteredClient dcrClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("dcr-admin-client")
                    .clientSecret(passwordEncoder.encode("dcr-secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("client.create")
                    .tokenSettings(tokenSettings)
                    .build();
            registeredClientRepository.save(dcrClient);
        }
    }
}
