package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));
        if (Boolean.TRUE.equals(role.getIsSystem())) {
            throw new IllegalStateException("System role cannot be deactivated");
        }
        role.setIsActive(false);
        role.setUpdatedAt(OffsetDateTime.now());
        role.setUpdatedBy(currentActorProvider.getCurrentActorSub());
        roleRepository.save(role);
    }
}

