package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    Optional<Role> findByName(String name);

    Optional<Role> findByUuid(UUID uuid);

    Page<Role> findAllByIsDeletedFalse(Pageable pageable);
}
