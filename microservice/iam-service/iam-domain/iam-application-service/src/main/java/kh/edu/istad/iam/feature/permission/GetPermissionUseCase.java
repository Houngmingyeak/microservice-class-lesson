package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.model.Permission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPermissionUseCase {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public PermissionResult execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permissionUuid));

        return new PermissionResult(
                permission.getUuid(),
                permission.getName(),
                permission.getResource(),
                permission.getAction(),
                permission.getDescription(),
                permission.getIsSystem(),
                permission.getIsActive(),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );
    }
}

