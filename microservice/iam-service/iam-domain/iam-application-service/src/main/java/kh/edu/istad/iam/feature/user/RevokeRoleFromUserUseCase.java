package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.role.RoleRepository;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevokeRoleFromUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID userUuid, UUID roleUuid, String reason) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));

        UserRole ur = userRepository.findUserRole(user.getId(), role.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User role assignment not found"));

        ur.setRevokedAt(OffsetDateTime.now());
        ur.setRevokedBy(currentActorProvider.getCurrentActorSub());
        ur.setReason(reason);
        userRepository.saveUserRole(ur);
    }
}

