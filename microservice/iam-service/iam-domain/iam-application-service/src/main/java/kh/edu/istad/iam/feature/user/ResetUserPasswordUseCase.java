package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public void execute(ResetUserPasswordCommand command) {
        execute(command.userUuid(), command.newPassword());
    }

    @Transactional
    public void execute(UUID userUuid, String newPassword) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        user.setPasswordHash(passwordHasher.hash(newPassword));
        user.setPasswordChangedAt(OffsetDateTime.now());
        user.setCredentialsNonExpired(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }
}
