package kh.edu.istad.iam.config;

import kh.edu.istad.iam.shared.CurrentActorProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Output port adapter: reads the actor from the Spring Security context.
 */
@Component
public class SecurityCurrentActorProvider implements CurrentActorProvider {

    @Override
    public String currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "system";
        }
        return authentication.getName();
    }
}