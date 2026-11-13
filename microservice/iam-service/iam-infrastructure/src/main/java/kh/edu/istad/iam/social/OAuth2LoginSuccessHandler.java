package kh.edu.istad.iam.social;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.iam.config.CustomUserDetails;
import kh.edu.istad.iam.feature.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.feature.identity.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles successful OAuth2 social logins (Google, GitHub, Facebook)
 * and provisions or finds federated identity users via FindOrProvisionFederatedUserUseCase.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauth2Auth) {
            String registrationId = oauth2Auth.getAuthorizedClientRegistrationId().toLowerCase();
            OAuth2User oauth2User = oauth2Auth.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            String provider = registrationId.toUpperCase();
            String providerSubject = null;
            String email = null;
            Boolean emailVerified = false;
            String displayName = null;
            String username = null;
            String pictureUrl = null;

            if ("google".equalsIgnoreCase(registrationId)) {
                providerSubject = (String) attributes.get("sub");
                email = (String) attributes.get("email");
                emailVerified = Boolean.TRUE.equals(attributes.get("email_verified"));
                displayName = (String) attributes.get("name");
                pictureUrl = (String) attributes.get("picture");
                username = email != null && email.contains("@") ? email.split("@")[0] : null;
            } else if ("github".equalsIgnoreCase(registrationId)) {
                Object idObj = attributes.get("id");
                providerSubject = idObj != null ? String.valueOf(idObj) : null;
                email = (String) attributes.get("email");
                username = (String) attributes.get("login");
                displayName = (String) attributes.get("name");
                if (displayName == null) {
                    displayName = username;
                }
                pictureUrl = (String) attributes.get("avatar_url");
                emailVerified = email != null;
            } else if ("facebook".equalsIgnoreCase(registrationId)) {
                providerSubject = (String) attributes.get("id");
                email = (String) attributes.get("email");
                displayName = (String) attributes.get("name");
                emailVerified = email != null;
            }

            if (providerSubject == null) {
                providerSubject = oauth2User.getName();
            }

            FederatedIdentityCommand command = new FederatedIdentityCommand(
                    provider,
                    providerSubject,
                    email,
                    emailVerified,
                    displayName,
                    username,
                    pictureUrl,
                    attributes
            );

            var authUserResult = findOrProvisionFederatedUserUseCase.execute(command);
            var domainUserOpt = userRepository.findByUuid(authUserResult.uuid());

            if (domainUserOpt.isPresent()) {
                Set<GrantedAuthority> authorities = authUserResult.authorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
                CustomUserDetails userDetails = new CustomUserDetails(domainUserOpt.get(), authorities);
                UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                );
                org.springframework.security.core.context.SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(newAuth);
                SecurityContextHolder.setContext(context);
                request.getSession().setAttribute(
                        org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
                );
            }
        }

        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

