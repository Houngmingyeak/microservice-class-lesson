package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Get a single permission by UUID.
 */
@Service
@RequiredArgsConstructor
public class GetPermissionByUuidUseCase {

    private final PermissionJpaRepository permissionJpaRepository;

    @Transactional(readOnly = true)
    public PermissionResponse execute(String uuid) {
        return permissionJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(CreatePermissionUseCase::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "uuid", uuid));
    }
}
