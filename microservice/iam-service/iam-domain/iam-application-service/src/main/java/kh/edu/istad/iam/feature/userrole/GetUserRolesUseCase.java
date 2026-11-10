package kh.edu.istad.iam.feature.userrole;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.role.CreateRoleUseCase;
import kh.edu.istad.iam.feature.role.RoleResponse;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case: Get active roles assigned to a user.
 */
@Service
@RequiredArgsConstructor
public class GetUserRolesUseCase {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Transactional(readOnly = true)
    public List<RoleResponse> execute(String userUuid) {
        User user = userJpaRepository.findByUuid(UUID.fromString(userUuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", userUuid));

        return userRoleJpaRepository.findActiveByUserId(user.getId())
                .stream()
                .map(ur -> CreateRoleUseCase.toResponse(ur.getRole()))
                .toList();
    }
}
