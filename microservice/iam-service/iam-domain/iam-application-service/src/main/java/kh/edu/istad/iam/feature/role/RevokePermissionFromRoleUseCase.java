package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.domain.RolePermission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.permission.PermissionJpaRepository;
import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Revoke (soft-revoke) a permission from a role.
 */
@Service
@RequiredArgsConstructor
public class RevokePermissionFromRoleUseCase {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;

    @Transactional
    public void execute(String roleUuid, String permissionUuid) {
        Role role = roleJpaRepository.findByUuid(UUID.fromString(roleUuid))
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "uuid", roleUuid));

        Permission permission = permissionJpaRepository.findByUuid(UUID.fromString(permissionUuid))
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "uuid", permissionUuid));

        RolePermission rolePermission = rolePermissionJpaRepository
                .findByRoleIdAndPermissionId(role.getId(), permission.getId())
                .orElseThrow(() -> new ResourceNotFoundException("RolePermission", "roleId+permissionId",
                        roleUuid + "+" + permissionUuid));

        rolePermission.setRevokedAt(OffsetDateTime.now());
        rolePermission.setRevokedBy("system");
        rolePermissionJpaRepository.save(rolePermission);
    }
}
