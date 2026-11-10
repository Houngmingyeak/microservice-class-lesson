package kh.edu.istad.iam.feature.rolepermission;

import kh.edu.istad.iam.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.permission WHERE rp.role.id = :roleId AND rp.revokedAt IS NULL")
    List<RolePermission> findActiveByRoleId(@Param("roleId") Long roleId);
}
