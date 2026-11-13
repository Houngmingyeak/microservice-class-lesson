package kh.edu.istad.iam.social;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.iam.config.CustomUserDetails;
import kh.edu.istad.iam.feature.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.feature.identity.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Endpoint for Telegram Login Widget callback (/login/oauth2/code/telegram).
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class TelegramAuthController {

    private final TelegramAuthValidator telegramAuthValidator;
    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final UserRepository userRepository;

    @GetMapping("/login/oauth2/code/telegram")
    public void telegramCallback(@RequestParam Map<String, String> allParams,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        if (!telegramAuthValidator.validate(allParams)) {
            log.warn("Telegram authentication failed validation with params: {}", allParams.keySet());
            response.sendRedirect("/login?error=telegram");
            return;
        }

        String id = allParams.get("id");
        String firstName = allParams.getOrDefault("first_name", "");
        String lastName = allParams.getOrDefault("last_name", "");
        String username = allParams.get("username");
        String photoUrl = allParams.get("photo_url");
        String displayName = (firstName + " " + lastName).trim();
        if (displayName.isBlank()) {
            displayName = username != null ? username : "Telegram User";
        }

        FederatedIdentityCommand command = new FederatedIdentityCommand(
                "TELEGRAM",
                id,
                username != null ? username + "@telegram.org" : null,
                true,
                displayName,
                username,
                photoUrl,
                java.util.Map.of()
        );

        var authUserResult = findOrProvisionFederatedUserUseCase.execute(command);
        var domainUserOpt = userRepository.findByUuid(authUserResult.uuid());

        if (domainUserOpt.isPresent()) {
            Set<GrantedAuthority> authorities = authUserResult.authorities().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            CustomUserDetails userDetails = new CustomUserDetails(domainUserOpt.get(), authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            request.getSession().setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
            );
        }

        response.sendRedirect("/");
    }
}

