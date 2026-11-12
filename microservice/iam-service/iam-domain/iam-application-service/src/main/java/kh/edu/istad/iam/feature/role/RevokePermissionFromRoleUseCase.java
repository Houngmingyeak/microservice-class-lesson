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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevokePermissionFromRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID roleUuid, UUID permissionUuid, String reason) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));
        var perm = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permissionUuid));

        RolePermission rp = roleRepository.findRolePermission(role.getId(), perm.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role permission assignment not found"));

        rp.setRevokedAt(OffsetDateTime.now());
        rp.setRevokedBy(currentActorProvider.getCurrentActorSub());
        rp.setReason(reason);
        roleRepository.saveRolePermission(rp);
    }
}

