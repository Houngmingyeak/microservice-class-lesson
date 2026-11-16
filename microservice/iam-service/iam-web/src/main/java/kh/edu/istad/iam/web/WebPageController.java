package kh.edu.istad.iam.web;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.feature.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.feature.role.RoleRepository;
import kh.edu.istad.iam.feature.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class WebPageController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final MessageSource messageSource;

    /**
     * Single Page Application route handler for React frontend.
     * Serves index.html for all frontend routes.
     */
    @GetMapping({"/", "/login", "/register", "/account/**", "/management/**"})
    public String reactApp() {
        return "forward:/index.html";
    }

    /**
     * REST endpoint for React to retrieve currently authenticated session user.
     */
    @GetMapping("/web-api/me")
    @ResponseBody
    public Map<String, Object> currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Map.of("authenticated", false);
        }

        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .toList();

        List<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .toList();

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("authenticated", true);
        res.put("username", username);
        res.put("email", userOpt.map(User::getEmail).orElse(username + "@example.com"));
        res.put("givenName", userOpt.map(User::getGivenName).orElse(username));
        res.put("familyName", userOpt.map(User::getFamilyName).orElse(""));
        res.put("nickname", userOpt.map(User::getNickname).orElse(""));
        res.put("phoneNumber", userOpt.map(User::getPhoneNumber).orElse(""));
        res.put("roles", roles.isEmpty() ? List.of("USER") : roles);
        res.put("permissions", permissions);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", userOpt.map(u -> u.getUuid().toString()).orElse(username));
        claims.put("preferred_username", username);
        userOpt.ifPresent(u -> {
            if (u.getEmail() != null) claims.put("email", u.getEmail());
            if (u.getGivenName() != null) claims.put("given_name", u.getGivenName());
            if (u.getFamilyName() != null) claims.put("family_name", u.getFamilyName());
        });
        res.put("claims", claims);

        return res;
    }

    /**
     * REST endpoint for user self-registration directly into PostgreSQL database.
     */
    @PostMapping("/web-api/auth/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String email = body.get("email");
            String password = body.get("password");
            String confirmPassword = body.getOrDefault("confirmPassword", password);
            String givenName = body.getOrDefault("givenName", "");
            String familyName = body.getOrDefault("familyName", "");
            String locale = body.getOrDefault("locale", "en");

            RegisterUserCommand command = new RegisterUserCommand(
                    username, email, password, confirmPassword, givenName, familyName, locale
            );
            UserResult result = registerUserUseCase.execute(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "User registered successfully",
                    "userId", result.uuid().toString(),
                    "username", result.username(),
                    "email", result.email()
            ));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", ex.getMessage()
            ));
        }
    }

    /**
     * REST endpoint to list users directly from PostgreSQL database for Admin Management.
     */
    @GetMapping("/web-api/users")
    @ResponseBody
    public List<Map<String, Object>> listAllUsers() {
        var users = userRepository.findAll(0, 100, "id", "asc");
        return users.stream().map(u -> {
            var activeRoles = userRepository.findActiveRolesByUserId(u.getId());
            List<String> roleNames = activeRoles.stream()
                    .map(ur -> roleRepository.findById(ur.getRoleId()).map(kh.edu.istad.iam.domain.model.Role::getName).orElse("USER"))
                    .toList();
            if (roleNames.isEmpty()) {
                roleNames = List.of("USER");
            }

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", u.getUuid().toString());
            map.put("uuid", u.getUuid().toString());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("givenName", u.getGivenName() != null ? u.getGivenName() : "");
            map.put("familyName", u.getFamilyName() != null ? u.getFamilyName() : "");
            map.put("phoneNumber", u.getPhoneNumber() != null ? u.getPhoneNumber() : "");
            map.put("roles", roleNames);
            map.put("enabled", Boolean.TRUE.equals(u.getEnabled()));
            map.put("accountNonLocked", Boolean.TRUE.equals(u.getAccountNonLocked()));
            map.put("createdAt", u.getCreatedAt() != null ? u.getCreatedAt().toString() : "");
            map.put("avatar", u.getPictureUrl());
            return map;
        }).toList();
    }

    /**
     * REST endpoint for Admin to create a new user directly into PostgreSQL database.
     */
    @PostMapping("/web-api/users")
    @ResponseBody
    public ResponseEntity<?> createUserByAdmin(@RequestBody Map<String, Object> body) {
        try {
            String username = (String) body.get("username");
            String email = (String) body.get("email");
            String password = (String) body.getOrDefault("password", "Password123!");
            String givenName = (String) body.getOrDefault("givenName", "");
            String familyName = (String) body.getOrDefault("familyName", "");
            String roleName = body.containsKey("role") ? (String) body.get("role") : "USER";

            RegisterUserCommand command = new RegisterUserCommand(
                    username, email, password, password, givenName, familyName, "en"
            );
            UserResult result = registerUserUseCase.execute(command);

            if (!"USER".equalsIgnoreCase(roleName)) {
                roleRepository.findByName(roleName.toUpperCase()).ifPresent(r -> {
                    userRepository.findByUuid(result.uuid()).ifPresent(u -> {
                        userRepository.saveUserRole(kh.edu.istad.iam.domain.model.UserRole.assign(
                                u.getId(), r.getId(), "admin", null, "Assigned by admin"
                        ));
                    });
                });
            }

            Map<String, Object> res = new LinkedHashMap<>();
            res.put("id", result.uuid().toString());
            res.put("uuid", result.uuid().toString());
            res.put("username", result.username());
            res.put("email", result.email());
            res.put("givenName", result.givenName());
            res.put("familyName", result.familyName());
            res.put("roles", List.of(roleName.toUpperCase()));
            res.put("enabled", true);
            res.put("accountNonLocked", true);
            res.put("createdAt", OffsetDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/web-api/users/{uuid}/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleUserStatus(@PathVariable UUID uuid) {
        Optional<User> uOpt = userRepository.findByUuid(uuid);
        if (uOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User u = uOpt.get();
        u.setEnabled(!Boolean.TRUE.equals(u.getEnabled()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("enabled", u.getEnabled()));
    }

    @PostMapping("/web-api/users/{uuid}/toggle-lock")
    @ResponseBody
    public ResponseEntity<?> toggleUserLock(@PathVariable UUID uuid) {
        Optional<User> uOpt = userRepository.findByUuid(uuid);
        if (uOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User u = uOpt.get();
        u.setAccountNonLocked(!Boolean.TRUE.equals(u.getAccountNonLocked()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("accountNonLocked", u.getAccountNonLocked()));
    }

    @DeleteMapping("/web-api/users/{uuid}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable UUID uuid) {
        Optional<User> uOpt = userRepository.findByUuid(uuid);
        if (uOpt.isPresent()) {
            User u = uOpt.get();
            u.setIsDeleted(true);
            u.setDeletedAt(OffsetDateTime.now());
            userRepository.save(u);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/web-api/users/{uuid}/roles")
    @ResponseBody
    public ResponseEntity<?> assignRole(@PathVariable UUID uuid, @RequestBody Map<String, Object> body) {
        Optional<User> uOpt = userRepository.findByUuid(uuid);
        if (uOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User u = uOpt.get();
        List<?> roles = (List<?>) body.get("roles");
        String reason = (String) body.getOrDefault("reason", "Admin role assignment");
        if (roles != null) {
            for (Object rObj : roles) {
                String rName = rObj.toString();
                roleRepository.findByName(rName.toUpperCase()).ifPresent(r -> {
                    userRepository.saveUserRole(kh.edu.istad.iam.domain.model.UserRole.assign(
                            u.getId(), r.getId(), "admin", null, reason
                    ));
                });
            }
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ---------------------------------------------------------
    // Legacy JTE Template Routes (Preserved under /jte)
    // ---------------------------------------------------------

    @GetMapping("/jte/login")
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

    @GetMapping("/jte/register")
    public String registerPage(CsrfToken csrfToken, Locale locale, Model model) {
        model.addAttribute("locale", locale.getLanguage());
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "register";
    }

    @GetMapping("/jte")
    public String index(Authentication authentication, CsrfToken csrfToken, Locale locale, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/jte/login";
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
                phoneNumber,
                null,
                null,
                null,
                null,
                locale.getLanguage()
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

