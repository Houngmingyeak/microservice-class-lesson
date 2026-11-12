package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.RolePermission;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public RoleResult execute(CreateRoleCommand command) {
        if (roleRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Role name already exists: " + command.name());
        }

        Role role = Role.create(command.name(), command.description(), false);
        Role saved = roleRepository.save(role);

        List<String> assignedPermNames = new ArrayList<>();
        if (command.permissionUuids() != null && !command.permissionUuids().isEmpty()) {
            String actor = currentActorProvider.getCurrentActorSub();
            for (var permUuid : command.permissionUuids()) {
                permissionRepository.findByUuid(permUuid).ifPresent(p -> {
                    RolePermission rp = new RolePermission();
                    rp.setRoleId(saved.getId());
                    rp.setPermissionId(p.getId());
                    rp.setAssignedAt(OffsetDateTime.now());
                    rp.setAssignedBy(actor);
                    roleRepository.saveRolePermission(rp);
                    assignedPermNames.add(p.getName());
                });
            }
        }

        return new RoleResult(
                saved.getUuid(),
                saved.getName(),
                saved.getDescription(),
                saved.getIsSystem(),
                saved.getIsActive(),
                assignedPermNames,
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}

