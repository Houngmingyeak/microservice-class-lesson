package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Soft-delete a permission by UUID.
 */
@Service
@RequiredArgsConstructor
public class DeletePermissionUseCase {

    private final PermissionJpaRepository permissionJpaRepository;

    @Transactional
    public void execute(String uuid) {
        Permission permission = permissionJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "uuid", uuid));

        permission.setIsDeleted(true);
        permission.setDeletedAt(OffsetDateTime.now());
        permissionJpaRepository.save(permission);
    }
}
