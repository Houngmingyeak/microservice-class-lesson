package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.model.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePermissionUseCase {

    private final PermissionRepository permissionRepository;

    @Transactional
    public PermissionResult execute(CreatePermissionCommand command) {
        String name = command.resource() + ":" + command.action();
        if (permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission name already exists: " + name);
        }

        Permission permission = Permission.create(command.resource(), command.action(), command.description(), false);
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

