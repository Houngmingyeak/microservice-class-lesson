package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Soft-delete a role by UUID.
 */
@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase {

    private final RoleJpaRepository roleJpaRepository;

    @Transactional
    public void execute(String uuid) {
        Role role = roleJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "uuid", uuid));

        role.setIsDeleted(true);
        role.setDeletedAt(OffsetDateTime.now());
        roleJpaRepository.save(role);
    }
}
