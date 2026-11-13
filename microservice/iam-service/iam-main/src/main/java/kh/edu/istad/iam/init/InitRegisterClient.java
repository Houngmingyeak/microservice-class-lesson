package kh.edu.istad.iam.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
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
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class InitRegisterClient implements ApplicationRunner {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        log.info("InitRegisterClient: Initializing OAuth2 registered clients...");

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofHours(5))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                .reuseRefreshTokens(false)
                .build();

        // 1. Regular Client (front-bff-client) as Confidential Client for Gateway BFF (Port 9999)
        var existingFront = registeredClientRepository.findByClientId("front-bff-client");
        RegisteredClient frontClient = RegisteredClient.withId(existingFront != null ? existingFront.getId() : UUID.randomUUID().toString())
                .clientId("front-bff-client")
                .clientName("Gateway BFF Client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:9999/login/oauth2/code/front-bff-client")
                .redirectUri("http://127.0.0.1:9999/login/oauth2/code/front-bff-client")
                .postLogoutRedirectUri("http://localhost:9999/")
                .postLogoutRedirectUri("http://127.0.0.1:9999/")
                .postLogoutRedirectUri("http://localhost:9999/login?logout")
                .postLogoutRedirectUri("http://localhost:39000/login")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.ADDRESS)
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(false)
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(tokenSettings)
                .build();
        registeredClientRepository.save(frontClient);
        log.info("InitRegisterClient: Registered client 'front-bff-client' ready.");

        // 2. Public Client (oidc-client) for SPA / Postman / Mobile with PKCE
        var existingOidc = registeredClientRepository.findByClientId("oidc-client");
        RegisteredClient oidcClient = RegisteredClient.withId(existingOidc != null ? existingOidc.getId() : UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientName("Public OIDC Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://localhost:8080/login/oauth2/code/oidc-client")
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .postLogoutRedirectUri("http://localhost:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(true)
                        .build())
                .tokenSettings(tokenSettings)
                .build();
        registeredClientRepository.save(oidcClient);
        log.info("InitRegisterClient: Registered client 'oidc-client' ready.");

        // 3. DCR Initial Access Token Client with scope client.create
        var existingDcr = registeredClientRepository.findByClientId("dcr-admin-client");
        RegisteredClient dcrClient = RegisteredClient.withId(existingDcr != null ? existingDcr.getId() : UUID.randomUUID().toString())
                .clientId("dcr-admin-client")
                .clientName("DCR Admin Client")
                .clientSecret(passwordEncoder.encode("dcr-secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("client.create")
                .tokenSettings(tokenSettings)
                .build();
        registeredClientRepository.save(dcrClient);
        log.info("InitRegisterClient: Registered client 'dcr-admin-client' ready.");
    }
}
