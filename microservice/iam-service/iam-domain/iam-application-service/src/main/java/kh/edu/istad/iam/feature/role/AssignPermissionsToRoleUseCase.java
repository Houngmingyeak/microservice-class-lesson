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
public class AssignPermissionsToRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<PermissionAssignmentResult> execute(AssignPermissionsToRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + command.roleUuid()));

        String actor = currentActorProvider.getCurrentActorSub();
        OffsetDateTime now = OffsetDateTime.now();
        List<PermissionAssignmentResult> results = new ArrayList<>();

        for (UUID permUuid : command.permissionUuids()) {
            var perm = permissionRepository.findByUuid(permUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permUuid));

            if (!Boolean.TRUE.equals(perm.getIsActive())) {
                throw new IllegalStateException("Inactive permission cannot be assigned: " + perm.getName());
            }

            var existing = roleRepository.findRolePermission(role.getId(), perm.getId());
            if (existing.isPresent()) {
                RolePermission rp = existing.get();
                rp.setRevokedAt(null);
                rp.setRevokedBy(null);
                rp.setAssignedAt(now);
                rp.setAssignedBy(actor);
                roleRepository.saveRolePermission(rp);
            } else {
                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(perm.getId());
                rp.setAssignedAt(now);
                rp.setAssignedBy(actor);
                roleRepository.saveRolePermission(rp);
            }

            results.add(new PermissionAssignmentResult(perm.getUuid(), perm.getName(), now, actor));
        }

        return results;
    }
}

