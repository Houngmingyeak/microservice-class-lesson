package kh.edu.istad.iam.config;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import kh.edu.istad.iam.feature.userpermission.UserPermissionJpaRepository;
import kh.edu.istad.iam.feature.userrole.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Load roles and their permissions
        userRoleJpaRepository.findActiveByUserId(user.getId()).forEach(userRole -> {
            String roleName = userRole.getRole().getName();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

            // Load permissions via role
            rolePermissionJpaRepository.findActiveByRoleId(userRole.getRole().getId()).forEach(rolePermission ->
                    authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getName()))
            );
        });

        // Load direct user permissions
        userPermissionJpaRepository.findActiveByUserId(user.getId()).forEach(userPermission -> {
            // Only add ALLOW permissions; skip DENY
            if (!"DENY".equalsIgnoreCase(userPermission.getEffect())) {
                authorities.add(new SimpleGrantedAuthority(userPermission.getPermission().getName()));
            }
        });

        return new CustomUserDetails(user, authorities);
    }
}
