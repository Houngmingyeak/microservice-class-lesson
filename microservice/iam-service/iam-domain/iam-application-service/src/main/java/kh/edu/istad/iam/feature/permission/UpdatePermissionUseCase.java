package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Update an existing permission by UUID.
 */
@Service
@RequiredArgsConstructor
public class UpdatePermissionUseCase {

    private final PermissionJpaRepository permissionJpaRepository;

    @Transactional
    public PermissionResponse execute(String uuid, UpdatePermissionRequest request) {
        Permission permission = permissionJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "uuid", uuid));

        if (request.name() != null) permission.setName(request.name());
        if (request.resource() != null) permission.setResource(request.resource());
        if (request.action() != null) permission.setAction(request.action());
        if (request.description() != null) permission.setDescription(request.description());
        if (request.isActive() != null) permission.setIsActive(request.isActive());

        permission = permissionJpaRepository.save(permission);
        return CreatePermissionUseCase.toResponse(permission);
    }
}
