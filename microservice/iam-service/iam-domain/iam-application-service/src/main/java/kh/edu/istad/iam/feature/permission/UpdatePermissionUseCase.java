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
public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public PermissionResult execute(UUID permissionUuid, UpdatePermissionCommand command) {
        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permissionUuid));

        if (command.description() != null) {
            permission.setDescription(command.description());
        }
        permission.setUpdatedAt(OffsetDateTime.now());
        permission.setUpdatedBy(currentActorProvider.getCurrentActorSub());
        Permission saved = permissionRepository.save(permission);

        return new PermissionResult(
                saved.getUuid(),
                saved.getName(),
                saved.getResource(),
                saved.getAction(),
                saved.getDescription(),
                saved.getIsSystem(),
                saved.getIsActive(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}

