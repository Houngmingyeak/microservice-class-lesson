package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Create a new role.
 */
@Service
@RequiredArgsConstructor
public class CreateRoleUseCase {

    private final RoleJpaRepository roleJpaRepository;

    @Transactional
    public RoleResponse execute(CreateRoleRequest request) {
        if (roleJpaRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Role already exists: " + request.name());
        }

        Role role = Role.builder()
                .uuid(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .isSystem(false)
                .isActive(true)
                .isDeleted(false)
                .build();

        role = roleJpaRepository.save(role);
        return toResponse(role);
    }

    static RoleResponse toResponse(Role role) {
        return new RoleResponse(role.getUuid(), role.getName(), role.getDescription(),
                role.getIsSystem(), role.getIsActive());
    }
}
