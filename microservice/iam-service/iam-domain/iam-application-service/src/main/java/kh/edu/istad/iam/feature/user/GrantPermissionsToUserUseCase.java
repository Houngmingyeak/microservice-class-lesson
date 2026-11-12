package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.Permission;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserPermission;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
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
public class GrantPermissionsToUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<PermissionGrantResult> execute(UUID userUuid, GrantPermissionsToUserCommand command) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        String actor = currentActorProvider.getCurrentActorSub();
        OffsetDateTime now = OffsetDateTime.now();
        List<PermissionGrantResult> results = new ArrayList<>();

        for (UUID permUuid : command.permissionUuids()) {
            Permission perm = permissionRepository.findByUuid(permUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permUuid));

            if (!Boolean.TRUE.equals(perm.getIsActive())) {
                throw new IllegalStateException("Cannot assign inactive permission: " + perm.getName());
            }

            var existing = userRepository.findUserPermission(user.getId(), perm.getId());
            if (existing.isPresent()) {
                UserPermission up = existing.get();
                up.setEffect(command.effect());
                up.setRevokedAt(null);
                up.setRevokedBy(null);
                up.setAssignedAt(now);
                up.setAssignedBy(actor);
                up.setExpiresAt(command.expiresAt());
                up.setReason(command.reason());
                userRepository.saveUserPermission(up);
            } else {
                UserPermission up = new UserPermission();
                up.setUserId(user.getId());
                up.setPermissionId(perm.getId());
                up.setEffect(command.effect());
                up.setAssignedAt(now);
                up.setAssignedBy(actor);
                up.setExpiresAt(command.expiresAt());
                up.setReason(command.reason());
                userRepository.saveUserPermission(up);
            }

            results.add(new PermissionGrantResult(perm.getUuid(), perm.getName(), command.effect(), command.expiresAt(), command.reason()));
        }

        return results;
    }
}

