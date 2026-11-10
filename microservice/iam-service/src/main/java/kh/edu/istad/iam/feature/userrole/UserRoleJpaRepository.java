package kh.edu.istad.iam.feature.userrole;

import kh.edu.istad.iam.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByRoleId(Long roleId);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.user.id = :userId AND ur.revokedAt IS NULL")
    List<UserRole> findActiveByUserId(@Param("userId") Long userId);
}
