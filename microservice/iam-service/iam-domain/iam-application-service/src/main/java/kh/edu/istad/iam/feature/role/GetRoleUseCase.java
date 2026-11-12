package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public RoleResult execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));

        List<String> permissions = roleRepository.findActivePermissionsByRoleId(role.getId()).stream()
                .map(rp -> permissionRepository.findById(rp.getPermissionId())
                        .map(kh.edu.istad.iam.domain.model.Permission::getName)
                        .orElse(null))
                .filter(p -> p != null)
                .toList();

        return new RoleResult(
                role.getUuid(),
                role.getName(),
                role.getDescription(),
                role.getIsSystem(),
                role.getIsActive(),
                permissions,
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}

