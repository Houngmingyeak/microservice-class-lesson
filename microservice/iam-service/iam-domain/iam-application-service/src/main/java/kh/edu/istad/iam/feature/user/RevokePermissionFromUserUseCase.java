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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID userUuid, UUID permissionUuid, String reason) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        Permission perm = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with uuid: " + permissionUuid));

        UserPermission up = userRepository.findUserPermission(user.getId(), perm.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User permission assignment not found"));

        up.setRevokedAt(OffsetDateTime.now());
        up.setRevokedBy(currentActorProvider.getCurrentActorSub());
        up.setReason(reason);
        userRepository.saveUserPermission(up);
    }
}

