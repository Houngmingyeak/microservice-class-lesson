package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.RolePermission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: Persistence operations for Role.
 * Implemented by iam-persistence.
 */
public interface RoleRepository {

    boolean existsByName(String name);

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByUuid(UUID uuid);

    Optional<Role> findByName(String name);

    List<Role> findAll(int page, int size);

    long count();

    List<RolePermission> findActivePermissionsByRoleId(Long roleId);

    void saveRolePermission(RolePermission rolePermission);

    Optional<RolePermission> findRolePermission(Long roleId, Long permissionId);
}

