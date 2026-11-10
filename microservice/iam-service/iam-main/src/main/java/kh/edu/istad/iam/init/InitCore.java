package kh.edu.istad.iam.init;

import jakarta.annotation.PostConstruct;
import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.domain.UserRole;
import kh.edu.istad.iam.domain.RolePermission;
import kh.edu.istad.iam.feature.permission.PermissionJpaRepository;
import kh.edu.istad.iam.feature.role.RoleJpaRepository;
import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import kh.edu.istad.iam.feature.userrole.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        final String adminUsername = "houngmigyeak";

        if (userJpaRepository.existsByUsername(adminUsername)) {
            log.info("InitCore: Admin user '{}' already exists, skipping initialization.", adminUsername);
            return;
        }

        log.info("InitCore: Initializing core data...");

        // Create permission
        Permission permission = permissionJpaRepository.save(
                Permission.builder()
                        .uuid(UUID.randomUUID())
                        .name("user:read:all")
                        .resource("user")
                        .action("read:all")
                        .description("Read all users")
                        .isSystem(true)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        // Create role
        Role role = roleJpaRepository.save(
                Role.builder()
                        .uuid(UUID.randomUUID())
                        .name("ADMIN")
                        .description("Administrator role")
                        .isSystem(true)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        // Assign permission to role
        rolePermissionJpaRepository.save(
                RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .assignedAt(OffsetDateTime.now())
                        .assignedBy("system")
                        .build()
        );

        // Create admin user
        User user = userJpaRepository.save(
                User.builder()
                        .uuid(UUID.randomUUID())
                        .familyName("Houng")
                        .givenName("Mingyeak")
                        .email("houngmingyeak@gmail.com")
                        .gender("Female")
                        .emailVerified(true)
                        .username(adminUsername)
                        .passwordHash(passwordEncoder.encode("supersecret"))
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .isDeleted(false)
                        .failedLoginAttempts(0)
                        .build()
        );

        // Assign role to user
        userRoleJpaRepository.save(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .assignedAt(OffsetDateTime.now())
                        .assignedBy("system")
                        .build()
        );

        log.info("InitCore: Core data initialization complete.");
    }
}
