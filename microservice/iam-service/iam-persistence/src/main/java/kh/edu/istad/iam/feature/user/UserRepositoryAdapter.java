package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserPermission;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.feature.permission.PermissionJpaRepository;
import kh.edu.istad.iam.feature.role.RoleJpaRepository;
import kh.edu.istad.iam.feature.userpermission.UserPermissionJpaRepository;
import kh.edu.istad.iam.feature.userrole.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port adapter: implements UserRepository with Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userPersistenceMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return userPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userJpaRepository.findByUsernameOrEmail(username, email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userJpaRepository.findAllByIsDeletedFalse(pageable)
                .getContent()
                .stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public List<UserRole> findActiveRolesByUserId(Long userId) {
        return userRoleJpaRepository.findActiveByUserId(userId).stream()
                .map(ur -> {
                    UserRole model = new UserRole();
                    model.setId(ur.getId());
                    model.setUserId(ur.getUser().getId());
                    model.setRoleId(ur.getRole().getId());
                    model.setAssignedAt(ur.getAssignedAt());
                    model.setAssignedBy(ur.getAssignedBy());
                    model.setExpiresAt(ur.getExpiresAt());
                    model.setRevokedAt(ur.getRevokedAt());
                    model.setRevokedBy(ur.getRevokedBy());
                    model.setReason(ur.getReason());
                    return model;
                })
                .toList();
    }

    @Override
    public List<UserPermission> findActivePermissionsByUserId(Long userId) {
        return userPermissionJpaRepository.findActiveByUserId(userId).stream()
                .map(up -> {
                    UserPermission model = new UserPermission();
                    model.setId(up.getId());
                    model.setUserId(up.getUser().getId());
                    model.setPermissionId(up.getPermission().getId());
                    model.setEffect(up.getEffect());
                    model.setAssignedAt(up.getAssignedAt());
                    model.setAssignedBy(up.getAssignedBy());
                    model.setExpiresAt(up.getExpiresAt());
                    model.setRevokedAt(up.getRevokedAt());
                    model.setRevokedBy(up.getRevokedBy());
                    model.setReason(up.getReason());
                    return model;
                })
                .toList();
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        var userEntity = userJpaRepository.findById(userRole.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userRole.getUserId()));
        var roleEntity = roleJpaRepository.findById(userRole.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + userRole.getRoleId()));

        kh.edu.istad.iam.domain.UserRole entity = kh.edu.istad.iam.domain.UserRole.builder()
                .id(userRole.getId())
                .user(userEntity)
                .role(roleEntity)
                .assignedAt(userRole.getAssignedAt() != null ? userRole.getAssignedAt() : OffsetDateTime.now())
                .assignedBy(userRole.getAssignedBy())
                .expiresAt(userRole.getExpiresAt())
                .revokedAt(userRole.getRevokedAt())
                .revokedBy(userRole.getRevokedBy())
                .reason(userRole.getReason())
                .build();
        userRoleJpaRepository.save(entity);
    }

    @Override
    public void saveUserPermission(UserPermission userPermission) {
        var userEntity = userJpaRepository.findById(userPermission.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userPermission.getUserId()));
        var permissionEntity = permissionJpaRepository.findById(userPermission.getPermissionId())
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + userPermission.getPermissionId()));

        kh.edu.istad.iam.domain.UserPermission entity = kh.edu.istad.iam.domain.UserPermission.builder()
                .id(userPermission.getId())
                .user(userEntity)
                .permission(permissionEntity)
                .effect(userPermission.getEffect())
                .assignedAt(userPermission.getAssignedAt() != null ? userPermission.getAssignedAt() : OffsetDateTime.now())
                .assignedBy(userPermission.getAssignedBy())
                .expiresAt(userPermission.getExpiresAt())
                .revokedAt(userPermission.getRevokedAt())
                .revokedBy(userPermission.getRevokedBy())
                .reason(userPermission.getReason())
                .build();
        userPermissionJpaRepository.save(entity);
    }

    @Override
    public Optional<UserRole> findUserRole(Long userId, Long roleId) {
        return userRoleJpaRepository.findByUserIdAndRoleId(userId, roleId)
                .map(ur -> {
                    UserRole model = new UserRole();
                    model.setId(ur.getId());
                    model.setUserId(ur.getUser().getId());
                    model.setRoleId(ur.getRole().getId());
                    model.setAssignedAt(ur.getAssignedAt());
                    model.setAssignedBy(ur.getAssignedBy());
                    model.setExpiresAt(ur.getExpiresAt());
                    model.setRevokedAt(ur.getRevokedAt());
                    model.setRevokedBy(ur.getRevokedBy());
                    model.setReason(ur.getReason());
                    return model;
                });
    }

    @Override
    public Optional<UserPermission> findUserPermission(Long userId, Long permissionId) {
        return userPermissionJpaRepository.findByUserIdAndPermissionId(userId, permissionId)
                .map(up -> {
                    UserPermission model = new UserPermission();
                    model.setId(up.getId());
                    model.setUserId(up.getUser().getId());
                    model.setPermissionId(up.getPermission().getId());
                    model.setEffect(up.getEffect());
                    model.setAssignedAt(up.getAssignedAt());
                    model.setAssignedBy(up.getAssignedBy());
                    model.setExpiresAt(up.getExpiresAt());
                    model.setRevokedAt(up.getRevokedAt());
                    model.setRevokedBy(up.getRevokedBy());
                    model.setReason(up.getReason());
                    return model;
                });
    }
}
