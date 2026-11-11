package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserPermission;
import kh.edu.istad.iam.domain.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: Persistence operations for User and user assignments.
 * Implemented by iam-persistence.
 */
public interface UserRepository {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findAll(int page, int size, String sortBy, String direction);

    long count();

    // Assignment helpers
    List<UserRole> findActiveRolesByUserId(Long userId);

    List<UserPermission> findActivePermissionsByUserId(Long userId);

    void saveUserRole(UserRole userRole);

    void saveUserPermission(UserPermission userPermission);

    Optional<UserRole> findUserRole(Long userId, Long roleId);

    Optional<UserPermission> findUserPermission(Long userId, Long permissionId);
}
