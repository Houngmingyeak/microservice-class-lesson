package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.identity.AuthUserResult;
import kh.edu.istad.iam.feature.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoadUserForAuthenticationUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public AuthUserResult execute(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + usernameOrEmail));

        Set<String> authorities = new HashSet<>();
        userRepository.findActiveRolesByUserId(user.getId()).forEach(ur -> {
            roleRepository.findById(ur.getRoleId()).ifPresent(r -> {
                authorities.add("ROLE_" + r.getName());
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

