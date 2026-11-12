package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class RecordLoginAttemptUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(String usernameOrEmail, boolean success) {
        userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).ifPresent(user -> {
            if (success) {
                user.setFailedLoginAttempts(0);
                user.setLastLoginAt(OffsetDateTime.now());
            } else {
                int attempts = user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() + 1 : 1;
                user.setFailedLoginAttempts(attempts);
                if (attempts >= 5) {
                    user.setAccountNonLocked(false);
                }
            }
            userRepository.save(user);
        });
    }
}

