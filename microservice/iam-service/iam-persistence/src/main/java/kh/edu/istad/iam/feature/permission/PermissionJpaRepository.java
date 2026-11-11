package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository extends JpaRepository<Permission, Long> {

    boolean existsByName(String name);

    Optional<Permission> findByName(String name);

    Optional<Permission> findByUuid(UUID uuid);

    Page<Permission> findAllByIsDeletedFalse(Pageable pageable);

    Permission save(Permission permission);
}
