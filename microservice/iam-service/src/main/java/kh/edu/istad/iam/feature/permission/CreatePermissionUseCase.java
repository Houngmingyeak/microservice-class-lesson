package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Create a new permission.
 */
@Service
@RequiredArgsConstructor
public class CreatePermissionUseCase {

    private final PermissionJpaRepository permissionJpaRepository;

    @Transactional
    public PermissionResponse execute(CreatePermissionRequest request) {
        if (permissionJpaRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Permission already exists: " + request.name());
        }

        Permission permission = Permission.builder()
                .uuid(UUID.randomUUID())
                .name(request.name())
                .resource(request.resource())
                .action(request.action())
                .description(request.description())
                .isSystem(false)
                .isActive(true)
                .isDeleted(false)
                .build();

        permission = permissionJpaRepository.save(permission);
        return toResponse(permission);
    }

    static PermissionResponse toResponse(Permission p) {
        return new PermissionResponse(p.getUuid(), p.getName(), p.getResource(),
                p.getAction(), p.getDescription(), p.getIsSystem(), p.getIsActive());
    }
}
