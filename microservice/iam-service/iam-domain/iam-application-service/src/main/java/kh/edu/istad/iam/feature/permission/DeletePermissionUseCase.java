package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.model.Permission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeletePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permissionUuid));
        if (Boolean.TRUE.equals(permission.getIsSystem())) {
            throw new IllegalStateException("System permission cannot be deleted");
        }
        permission.setIsDeleted(true);
        permission.setDeletedAt(OffsetDateTime.now());
        permission.setDeletedBy(currentActorProvider.getCurrentActorSub());
        permissionRepository.save(permission);
    }
}

