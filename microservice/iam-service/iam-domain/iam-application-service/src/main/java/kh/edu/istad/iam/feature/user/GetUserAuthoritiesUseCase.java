package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import kh.edu.istad.iam.feature.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GetUserAuthoritiesUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public UserAuthoritiesResult execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        OffsetDateTime now = OffsetDateTime.now();
        List<UserAuthoritiesResult.RoleAuthority> roleAuthorities = new ArrayList<>();
        List<UserAuthoritiesResult.DirectPermission> directPerms = new ArrayList<>();
        Set<String> effectivePermissions = new LinkedHashSet<>();
        Set<String> deniedPermissions = new HashSet<>();

        // 1. Roles and their permissions
        userRepository.findActiveRolesByUserId(user.getId()).forEach(ur -> {
            if (ur.getExpiresAt() == null || ur.getExpiresAt().isAfter(now)) {
                roleRepository.findById(ur.getRoleId()).ifPresent(role -> {
                    roleAuthorities.add(new UserAuthoritiesResult.RoleAuthority(role.getUuid(), role.getName(), ur.getExpiresAt()));

                    roleRepository.findActivePermissionsByRoleId(role.getId()).forEach(rp -> {
                        if (rp.getExpiresAt() == null || rp.getExpiresAt().isAfter(now)) {
                            permissionRepository.findById(rp.getPermissionId()).ifPresent(p -> {
                                effectivePermissions.add(p.getName());
                            });
                        }
                    });
                });
            }
        });

        // 2. Direct user permissions: GRANT vs DENY
        userRepository.findActivePermissionsByUserId(user.getId()).forEach(up -> {
            if (up.getExpiresAt() == null || up.getExpiresAt().isAfter(now)) {
                permissionRepository.findById(up.getPermissionId()).ifPresent(p -> {
                    directPerms.add(new UserAuthoritiesResult.DirectPermission(p.getName(), up.getEffect()));
                    if ("DENY".equalsIgnoreCase(up.getEffect())) {
                        deniedPermissions.add(p.getName());
                    } else {
                        effectivePermissions.add(p.getName());
                    }
                });
            }
        });

        // Effective = active role permissions + direct GRANT - direct DENY
        effectivePermissions.removeAll(deniedPermissions);

        return new UserAuthoritiesResult(
                roleAuthorities,
                directPerms,
                new ArrayList<>(effectivePermissions)
        );
    }
}

