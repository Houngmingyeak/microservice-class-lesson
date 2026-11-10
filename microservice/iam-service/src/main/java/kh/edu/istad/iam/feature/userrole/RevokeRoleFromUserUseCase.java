package kh.edu.istad.iam.feature.userrole;

import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.domain.UserRole;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.role.RoleJpaRepository;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Soft-revoke a role from a user.
 */
@Service
@RequiredArgsConstructor
public class RevokeRoleFromUserUseCase {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Transactional
    public void execute(String userUuid, String roleUuid) {
        User user = userJpaRepository.findByUuid(UUID.fromString(userUuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", userUuid));

        Role role = roleJpaRepository.findByUuid(UUID.fromString(roleUuid))
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "uuid", roleUuid));

        UserRole userRole = userRoleJpaRepository.findByUserIdAndRoleId(user.getId(), role.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserRole", "userId+roleId",
                        userUuid + "+" + roleUuid));

        userRole.setRevokedAt(OffsetDateTime.now());
        userRole.setRevokedBy("system");
        userRoleJpaRepository.save(userRole);
    }
}
