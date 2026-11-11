package kh.edu.istad.iam.init;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.domain.RolePermission;
import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.domain.UserRole;
import kh.edu.istad.iam.feature.permission.PermissionJpaRepository;
import kh.edu.istad.iam.feature.role.RoleJpaRepository;
import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import kh.edu.istad.iam.feature.userrole.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitCore implements ApplicationRunner {

    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        final String adminUsername = "houngmingyeak";

        if (userJpaRepository.existsByUsername(adminUsername)) {
            log.info("InitCore: Admin user '{}' already exists, skipping.", adminUsername);
            return;
        }

        log.info("InitCore: Initializing core data...");

        // Permissions for user, role, permission
        List<String[]> perms = List.of(
                new String[]{"user", "create", "Create users"},
                new String[]{"user", "read", "Read users"},
                new String[]{"user", "update", "Update users"},
                new String[]{"user", "delete", "Delete users"},
                new String[]{"user", "assign", "Assign roles/permissions to users"},
                new String[]{"role", "create", "Create roles"},
                new String[]{"role", "read", "Read roles"},
                new String[]{"role", "update", "Update roles"},
                new String[]{"role", "delete", "Delete roles"},
                new String[]{"role", "assign", "Assign permissions to roles"},
                new String[]{"permission", "create", "Create permissions"},
                new String[]{"permission", "read", "Read permissions"},
                new String[]{"permission", "update", "Update permissions"},
                new String[]{"permission", "delete", "Delete permissions"}
        );

        Role adminRole = roleJpaRepository.save(
                Role.builder()
                        .uuid(UUID.randomUUID())
                        .name("ADMIN")
                        .description("Administrator role with full access")
                        .isSystem(true)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        Role userRole = roleJpaRepository.save(
                Role.builder()
                        .uuid(UUID.randomUUID())
                        .name("USER")
                        .description("Default user role")
                        .isSystem(true)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        for (String[] p : perms) {
            Permission permission = permissionJpaRepository.save(
                    Permission.builder()
                            .uuid(UUID.randomUUID())
                            .name(p[0] + ":" + p[1])
                            .resource(p[0])
                            .action(p[1])
                            .description(p[2])
                            .isSystem(true)
                            .isActive(true)
                            .isDeleted(false)
                            .build()
            );

            // Assign all permissions to ADMIN role
            rolePermissionJpaRepository.save(
                    RolePermission.builder()
                            .role(adminRole)
                            .permission(permission)
                            .assignedAt(OffsetDateTime.now())
                            .assignedBy("system")
                            .build()
            );

            // Assign read permission to USER role
            if ("read".equals(p[1]) && "user".equals(p[0])) {
                rolePermissionJpaRepository.save(
                        RolePermission.builder()
                                .role(userRole)
                                .permission(permission)
                                .assignedAt(OffsetDateTime.now())
                                .assignedBy("system")
                                .build()
                );
            }
        }

        UserEntity user = userJpaRepository.save(
                UserEntity.builder()
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

        userRoleJpaRepository.save(
                UserRole.builder()
                        .user(user)
                        .role(adminRole)
                        .assignedAt(OffsetDateTime.now())
                        .assignedBy("system")
                        .build()
        );

        log.info("InitCore: Core data initialization complete with ADMIN and USER roles.");
    }
}
