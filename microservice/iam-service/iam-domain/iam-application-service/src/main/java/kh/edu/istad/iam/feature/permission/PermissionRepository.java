package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: Persistence operations for Permission.
 * Implemented by iam-persistence.
 */
public interface PermissionRepository {

    boolean existsByName(String name);

    Permission save(Permission permission);

    Optional<Permission> findById(Long id);

    Optional<Permission> findByUuid(UUID uuid);

    Optional<Permission> findByName(String name);

    List<Permission> findAll(int page, int size);

    long count();
}

