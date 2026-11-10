package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Get a single role by UUID.
 */
@Service
@RequiredArgsConstructor
public class GetRoleByUuidUseCase {

    private final RoleJpaRepository roleJpaRepository;

    @Transactional(readOnly = true)
    public RoleResponse execute(String uuid) {
        return roleJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .map(CreateRoleUseCase::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "uuid", uuid));
    }
}
