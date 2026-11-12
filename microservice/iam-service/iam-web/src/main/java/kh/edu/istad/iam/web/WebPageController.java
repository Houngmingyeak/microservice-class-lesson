package kh.edu.istad.iam.web;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.feature.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.feature.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class WebPageController {

    private final UserRepository userRepository;
    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final MessageSource messageSource;

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            CsrfToken csrfToken,
            Locale locale,
            Model model
    ) {
        model.addAttribute("hasError", error != null);
        model.addAttribute("loggedOut", logout != null);
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        model.addAttribute("locale", locale.getLanguage());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(CsrfToken csrfToken, Locale locale, Model model) {
        model.addAttribute("locale", locale.getLanguage());
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String givenName,
            @RequestParam String familyName,
            CsrfToken csrfToken,
            Locale locale,
            Model model
    ) {
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        model.addAttribute("locale", locale.getLanguage());

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        try {
            RegisterUserCommand command = new RegisterUserCommand(
                    username,
                    email,
                    password,
                    confirmPassword,
                    givenName,
                    familyName,
                    locale.getLanguage()
            );
            registerUserUseCase.execute(command);
            return "redirect:/login?registered=true";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/oauth2/consent")
    public String consent(
            @RequestParam(value = "client_id", required = false, defaultValue = "Client Application") String clientName,
            @RequestParam(value = "scope", required = false, defaultValue = "openid profile email") String scopes,
            Locale locale,
            Model model
    ) {
        model.addAttribute("title", "Authorization Consent");
        model.addAttribute("clientName", clientName);
        model.addAttribute("scopes", scopes);
        model.addAttribute("locale", locale.getLanguage());
        return "consent";
    }

    @GetMapping("/")
    public String index(Authentication authentication, CsrfToken csrfToken, Locale locale, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);

        model.addAttribute("title", "My Profile");
        model.addAttribute("locale", locale.getLanguage());
        model.addAttribute("username", username);
        model.addAttribute("email", userOpt.map(User::getEmail).orElse(""));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .toList();
        List<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .toList();

        model.addAttribute("roles", roles);
        model.addAttribute("permissions", permissions);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", userOpt.map(u -> u.getUuid().toString()).orElse(username));
        claims.put("preferred_username", username);
        userOpt.ifPresent(u -> {
            if (u.getEmail() != null) claims.put("email", u.getEmail());
            if (u.getGivenName() != null) claims.put("given_name", u.getGivenName());
            if (u.getFamilyName() != null) claims.put("family_name", u.getFamilyName());
        });
        model.addAttribute("claims", claims);

        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }

        return "index";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Authentication authentication, CsrfToken csrfToken, Locale locale, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("title", "Edit Profile");
        model.addAttribute("locale", locale.getLanguage());
        model.addAttribute("givenName", user.getGivenName());
        model.addAttribute("familyName", user.getFamilyName());
        model.addAttribute("nickname", user.getNickname() != null ? user.getNickname() : "");
        model.addAttribute("phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String handleEditProfile(
            Authentication authentication,
            @RequestParam(required = false) String givenName,
            @RequestParam(required = false) String familyName,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phoneNumber,
            CsrfToken csrfToken,
            Locale locale,
            Model model
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                givenName,
                familyName,
                null,
                nickname,
                null,
                null,
                null,
                null,
                null,
                locale.getLanguage(),
                phoneNumber,
                null,
                null,
                null,
                null,
                null,
                null
        );
        updateUserProfileUseCase.execute(user.getUuid(), command);

        model.addAttribute("title", "Edit Profile");
        model.addAttribute("locale", locale.getLanguage());
        model.addAttribute("success", "Profile updated successfully!");
        model.addAttribute("givenName", givenName);
        model.addAttribute("familyName", familyName);
        model.addAttribute("nickname", nickname);
        model.addAttribute("phoneNumber", phoneNumber);
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "profile-edit";
    }

    @GetMapping("/profile/password")
    public String changePasswordPage(CsrfToken csrfToken, Locale locale, Model model) {
        model.addAttribute("title", "Change Password");
        model.addAttribute("locale", locale.getLanguage());
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String handleChangePassword(
            Authentication authentication,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            CsrfToken csrfToken,
            Locale locale,
            Model model
    ) {
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        model.addAttribute("title", "Change Password");
        model.addAttribute("locale", locale.getLanguage());

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "change-password";
        }

        try {
            String username = authentication.getName();
            User user = userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            changePasswordUseCase.execute(user.getUuid(), currentPassword, newPassword, confirmPassword);
            model.addAttribute("success", "Password changed successfully!");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "change-password";
    }

    @GetMapping("/profile/accounts")
    public String linkedAccountsPage(Authentication authentication, CsrfToken csrfToken, Locale locale, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var identities = listUserIdentitiesUseCase.execute(user.getUuid());

        model.addAttribute("title", "Linked Accounts");
        model.addAttribute("locale", locale.getLanguage());
        model.addAttribute("identities", identities);
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "linked-accounts";
    }

    @GetMapping("/profile/apps")
    public String connectedAppsPage(CsrfToken csrfToken, Locale locale, Model model) {
        model.addAttribute("title", "Connected Applications");
        model.addAttribute("locale", locale.getLanguage());
        model.addAttribute("apps", List.of("front-bff-client (Port 9999)"));
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "connected-apps";
    }
}

