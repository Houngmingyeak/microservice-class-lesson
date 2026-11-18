package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("""
        SELECT u FROM UserEntity u 
        WHERE (LOWER(TRIM(u.username)) = LOWER(TRIM(:username)) OR LOWER(TRIM(u.email)) = LOWER(TRIM(:email))) 
          AND (u.isDeleted IS NULL OR u.isDeleted = false)
        ORDER BY u.id ASC
    """)
    List<UserEntity> findAllByUsernameOrEmailIgnoreCase(@Param("username") String username, @Param("email") String email);

    default Optional<UserEntity> findByUsernameOrEmail(String username, String email) {
        if ((username == null || username.isBlank()) && (email == null || email.isBlank())) {
            return Optional.empty();
        }
        String u = username != null ? username.trim() : "";
        String e = email != null ? email.trim() : "";
        List<UserEntity> list = findAllByUsernameOrEmailIgnoreCase(u, e);
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.stream()
                .filter(usr -> u.equalsIgnoreCase(usr.getUsername()))
                .findFirst()
                .orElse(list.get(0)));
    }

    Optional<UserEntity> findByUuid(UUID uuid);

    Page<UserEntity> findAllByIsDeletedFalse(Pageable pageable);
}
