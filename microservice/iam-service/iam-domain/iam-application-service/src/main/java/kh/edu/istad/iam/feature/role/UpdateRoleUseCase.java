package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.RolePermission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public RoleResult execute(UUID roleUuid, UpdateRoleCommand command) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));

        if (command.description() != null) {
            role.setDescription(command.description());
        }
        role.setUpdatedAt(OffsetDateTime.now());
        role.setUpdatedBy(currentActorProvider.getCurrentActorSub());
        Role saved = roleRepository.save(role);

        if (command.permissionUuids() != null) {
            String actor = currentActorProvider.getCurrentActorSub();
            for (var permUuid : command.permissionUuids()) {
                permissionRepository.findByUuid(permUuid).ifPresent(p -> {
                    if (roleRepository.findRolePermission(saved.getId(), p.getId()).isEmpty()) {
                        RolePermission rp = new RolePermission();
                        rp.setRoleId(saved.getId());
                        rp.setPermissionId(p.getId());
                        rp.setAssignedAt(OffsetDateTime.now());
                        rp.setAssignedBy(actor);
                        roleRepository.saveRolePermission(rp);
                    }
                });
            }
        }

        List<String> permissions = roleRepository.findActivePermissionsByRoleId(saved.getId()).stream()
                .map(rp -> permissionRepository.findById(rp.getPermissionId())
                        .map(kh.edu.istad.iam.domain.model.Permission::getName)
                        .orElse(null))
                .filter(p -> p != null)
                .toList();

        return new RoleResult(
                saved.getUuid(),
                saved.getName(),
                saved.getDescription(),
                saved.getIsSystem(),
                saved.getIsActive(),
                permissions,
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}

