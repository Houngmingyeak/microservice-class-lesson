package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Soft-delete a user by UUID.
 */
@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void execute(String uuid) {
        User user = userJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", uuid));

        user.setIsDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());
        user.setEnabled(false);
        userJpaRepository.save(user);
    }
}
