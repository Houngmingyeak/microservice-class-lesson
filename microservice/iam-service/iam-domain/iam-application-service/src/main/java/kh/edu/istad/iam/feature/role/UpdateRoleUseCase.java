package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Update an existing role by UUID.
 */
@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleJpaRepository roleJpaRepository;

    @Transactional
    public RoleResponse execute(String uuid, UpdateRoleRequest request) {
        Role role = roleJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "uuid", uuid));

        if (request.name() != null) role.setName(request.name());
        if (request.description() != null) role.setDescription(request.description());
        if (request.isActive() != null) role.setIsActive(request.isActive());

        role = roleJpaRepository.save(role);
        return CreateRoleUseCase.toResponse(role);
    }
}
