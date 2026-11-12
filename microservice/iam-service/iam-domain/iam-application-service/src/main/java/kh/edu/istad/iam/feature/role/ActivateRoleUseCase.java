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
public class ActivateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));
        role.setIsActive(true);
        role.setUpdatedAt(OffsetDateTime.now());
        role.setUpdatedBy(currentActorProvider.getCurrentActorSub());
        roleRepository.save(role);
    }
}

