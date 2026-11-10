package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case: Change user password.
 */
@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(String uuid, ChangePasswordRequest request) {
        User user = userJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", uuid));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChangedAt(OffsetDateTime.now());
        userJpaRepository.save(user);
    }
}
