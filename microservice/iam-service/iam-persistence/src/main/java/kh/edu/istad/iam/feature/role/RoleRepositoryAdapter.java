package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.domain.model.RolePermission;
import kh.edu.istad.iam.feature.permission.PermissionJpaRepository;
import kh.edu.istad.iam.feature.rolepermission.RolePermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    @Override
    public kh.edu.istad.iam.domain.model.Role save(kh.edu.istad.iam.domain.model.Role role) {
        Role entity = toEntity(role);
        Role saved = roleJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Role> findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuid(uuid).map(this::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<kh.edu.istad.iam.domain.model.Role> findAll(int page, int size) {
        return roleJpaRepository.findAllByIsDeletedFalse(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return roleJpaRepository.count();
    }

    @Override
    public List<RolePermission> findActivePermissionsByRoleId(Long roleId) {
        return rolePermissionJpaRepository.findActiveByRoleId(roleId).stream()
                .map(rp -> {
                    RolePermission model = new RolePermission();
                    model.setId(rp.getId());
                    model.setRoleId(rp.getRole().getId());
                    model.setPermissionId(rp.getPermission().getId());
                    model.setAssignedAt(rp.getAssignedAt());
                    model.setAssignedBy(rp.getAssignedBy());
                    model.setExpiresAt(rp.getExpiresAt());
                    model.setRevokedAt(rp.getRevokedAt());
                    model.setRevokedBy(rp.getRevokedBy());
                    model.setReason(rp.getReason());
                    return model;
                })
                .toList();
    }

    @Override
    public void saveRolePermission(RolePermission rolePermission) {
        var role = roleJpaRepository.findById(rolePermission.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + rolePermission.getRoleId()));
        var permission = permissionJpaRepository.findById(rolePermission.getPermissionId())
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + rolePermission.getPermissionId()));

        kh.edu.istad.iam.domain.RolePermission entity = kh.edu.istad.iam.domain.RolePermission.builder()
                .id(rolePermission.getId())
                .role(role)
                .permission(permission)
                .assignedAt(rolePermission.getAssignedAt() != null ? rolePermission.getAssignedAt() : OffsetDateTime.now())
                .assignedBy(rolePermission.getAssignedBy())
                .expiresAt(rolePermission.getExpiresAt())
                .revokedAt(rolePermission.getRevokedAt())
                .revokedBy(rolePermission.getRevokedBy())
                .reason(rolePermission.getReason())
                .build();
        rolePermissionJpaRepository.save(entity);
    }

    @Override
    public Optional<RolePermission> findRolePermission(Long roleId, Long permissionId) {
        return rolePermissionJpaRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .map(rp -> {
                    RolePermission model = new RolePermission();
                    model.setId(rp.getId());
                    model.setRoleId(rp.getRole().getId());
                    model.setPermissionId(rp.getPermission().getId());
                    model.setAssignedAt(rp.getAssignedAt());
                    model.setAssignedBy(rp.getAssignedBy());
                    model.setExpiresAt(rp.getExpiresAt());
                    model.setRevokedAt(rp.getRevokedAt());
                    model.setRevokedBy(rp.getRevokedBy());
                    model.setReason(rp.getReason());
                    return model;
                });
    }

    private kh.edu.istad.iam.domain.model.Role toDomain(Role entity) {
        kh.edu.istad.iam.domain.model.Role model = new kh.edu.istad.iam.domain.model.Role();
        model.setId(entity.getId());
        model.setUuid(entity.getUuid());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setIsSystem(entity.getIsSystem());
        model.setIsActive(entity.getIsActive());
        model.setIsDeleted(entity.getIsDeleted());
        model.setDeletedAt(entity.getDeletedAt());
        model.setDeletedBy(entity.getDeletedBy());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setVersion(entity.getVersion());
        return model;
    }

    private Role toEntity(kh.edu.istad.iam.domain.model.Role model) {
        Role entity = new Role();
        entity.setId(model.getId());
        entity.setUuid(model.getUuid());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setIsSystem(model.getIsSystem());
        entity.setIsActive(model.getIsActive());
        entity.setIsDeleted(model.getIsDeleted());
        entity.setDeletedAt(model.getDeletedAt());
        entity.setDeletedBy(model.getDeletedBy());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setVersion(model.getVersion());
        return entity;
    }
}

