package kh.edu.istad.iam.config;

import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final kh.edu.istad.iam.feature.userrole.UserRoleJpaRepository userRoleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final kh.edu.istad.iam.feature.userpermission.UserPermissionJpaRepository userPermissionJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var entity = userJpaRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> effectivePermissions = new HashSet<>();
        Set<String> deniedPermissions = new HashSet<>();
        OffsetDateTime now = OffsetDateTime.now();

        // 1. Roles & Role-based Permissions
        userRoleJpaRepository.findActiveByUserId(entity.getId()).forEach(ur -> {
            if (ur.getExpiresAt() == null || ur.getExpiresAt().isAfter(now)) {
                String roleName = ur.getRole().getName();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

                // Load active permissions for this role
                rolePermissionJpaRepository.findActiveByRoleId(ur.getRole().getId()).forEach(rp -> {
                    if (rp.getExpiresAt() == null || rp.getExpiresAt().isAfter(now)) {
                        effectivePermissions.add(rp.getPermission().getName());
                    }
                });
            }
        });

        // 2. Direct User Permissions: GRANT vs DENY
        userPermissionJpaRepository.findActiveByUserId(entity.getId()).forEach(up -> {
            if (up.getExpiresAt() == null || up.getExpiresAt().isAfter(now)) {
                String permName = up.getPermission().getName();
                if ("DENY".equalsIgnoreCase(up.getEffect())) {
                    deniedPermissions.add(permName);
                } else {
                    effectivePermissions.add(permName);
                }
            }
        });

        // Calculate: Active role permissions + direct GRANT - direct DENY
        effectivePermissions.removeAll(deniedPermissions);
        effectivePermissions.forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));

        return User.builder()
                .username(entity.getUsername())
                .password(entity.getPasswordHash())
                .disabled(!Boolean.TRUE.equals(entity.getEnabled()))
                .accountExpired(!Boolean.TRUE.equals(entity.getAccountNonExpired()))
                .accountLocked(!Boolean.TRUE.equals(entity.getAccountNonLocked()))
                .credentialsExpired(!Boolean.TRUE.equals(entity.getCredentialsNonExpired()))
                .authorities(authorities)
                .build();
    }
}
