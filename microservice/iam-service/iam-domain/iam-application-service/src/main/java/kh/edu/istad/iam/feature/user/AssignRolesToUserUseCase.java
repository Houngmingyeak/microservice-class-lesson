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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignRolesToUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<RoleAssignmentResult> execute(UUID userUuid, AssignRolesToUserCommand command) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        String actor = currentActorProvider.getCurrentActorSub();
        OffsetDateTime now = OffsetDateTime.now();
        List<RoleAssignmentResult> results = new ArrayList<>();

        for (UUID roleUuid : command.roleUuids()) {
            Role role = roleRepository.findByUuid(roleUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with uuid: " + roleUuid));

            if (!Boolean.TRUE.equals(role.getIsActive())) {
                throw new IllegalStateException("Cannot assign inactive role: " + role.getName());
            }

            var existing = userRepository.findUserRole(user.getId(), role.getId());
            if (existing.isPresent()) {
                UserRole ur = existing.get();
                ur.setRevokedAt(null);
                ur.setRevokedBy(null);
                ur.setAssignedAt(now);
                ur.setAssignedBy(actor);
                ur.setExpiresAt(command.expiresAt());
                ur.setReason(command.reason());
                userRepository.saveUserRole(ur);
            } else {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(role.getId());
                ur.setAssignedAt(now);
                ur.setAssignedBy(actor);
                ur.setExpiresAt(command.expiresAt());
                ur.setReason(command.reason());
                userRepository.saveUserRole(ur);
            }

            results.add(new RoleAssignmentResult(role.getUuid(), role.getName(), command.expiresAt(), command.reason()));
        }

        return results;
    }
}

