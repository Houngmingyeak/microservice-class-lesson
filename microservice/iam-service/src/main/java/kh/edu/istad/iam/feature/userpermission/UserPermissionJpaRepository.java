package kh.edu.istad.iam.feature.userpermission;

import kh.edu.istad.iam.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);

    List<UserPermission> findByPermissionId(Long permissionId);

    Optional<UserPermission> findByUserIdAndPermissionId(Long userId, Long permissionId);

    boolean existsByUserIdAndPermissionId(Long userId, Long permissionId);

    @Query("SELECT up FROM UserPermission up JOIN FETCH up.permission WHERE up.user.id = :userId AND up.revokedAt IS NULL")
    List<UserPermission> findActiveByUserId(@Param("userId") Long userId);
}
