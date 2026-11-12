package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.feature.role.RoleRepository;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public AuthUserResult execute(FederatedIdentityCommand command) {
        Optional<Identity> existingIdentity = identityRepository
                .findByProviderAndProviderSubject(command.provider(), command.providerSubject());

        User user;
        if (existingIdentity.isPresent()) {
            Identity identity = existingIdentity.get();
            identity.setLastLoginAt(OffsetDateTime.now());
            identityRepository.save(identity);
            user = userRepository.findById(identity.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found for existing identity"));
        } else {
            // Check if user exists by email
            Optional<User> userByEmail = (command.email() != null && !command.email().isBlank())
                    ? userRepository.findByUsernameOrEmail(command.email(), command.email())
                    : Optional.empty();

            if (userByEmail.isPresent()) {
                user = userByEmail.get();
            } else {
                // Provision new user
                String username = (command.username() != null && !command.username().isBlank())
                        ? command.username()
                        : "user_" + UUID.randomUUID().toString().substring(0, 8);
                if (userRepository.existsByUsername(username)) {
                    username = username + "_" + (System.currentTimeMillis() % 10000);
                }

                String email = (command.email() != null && !command.email().isBlank())
                        ? command.email()
                        : username + "@placeholder.local";

                user = User.register(
                        username,
                        email,
                        null, // social-only, no password
                        command.displayName() != null ? command.displayName() : "User",
                        "",
                        "other",
                        null,
                        null
                );
                user.setEmailVerified(Boolean.TRUE.equals(command.emailVerified()));
                user.setPictureUrl(command.pictureUrl());
                user = userRepository.save(user);

                // Assign default USER role
                final User finalUser = user;
                roleRepository.findByName("USER").ifPresent(role -> {
                    UserRole ur = new UserRole();
                    ur.setUserId(finalUser.getId());
                    ur.setRoleId(role.getId());
                    ur.setAssignedAt(OffsetDateTime.now());
                    ur.setAssignedBy("system");
                    userRepository.saveUserRole(ur);
                });
            }

            // Link new identity
            Identity identity = Identity.link(
                    user.getId(),
                    command.provider(),
                    command.providerSubject(),
                    command.email(),
                    command.emailVerified(),
                    command.displayName(),
                    command.username(),
                    command.pictureUrl(),
                    command.attributes()
            );
            identity.setLastLoginAt(OffsetDateTime.now());
            identityRepository.save(identity);
        }

        // Calculate authorities
        Set<String> authorities = new HashSet<>();
        userRepository.findActiveRolesByUserId(user.getId()).forEach(ur -> {
            roleRepository.findById(ur.getRoleId()).ifPresent(r -> {
                authorities.add("ROLE_" + r.getName());
                roleRepository.findActivePermissionsByRoleId(r.getId()).forEach(rp -> {
                    // role permission
                });
            });
        });

        return new AuthUserResult(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                authorities
        );
    }
}

