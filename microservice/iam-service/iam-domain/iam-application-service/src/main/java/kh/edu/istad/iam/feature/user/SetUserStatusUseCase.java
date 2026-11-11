package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SetUserStatusUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void setEnabled(UUID userUuid, boolean enabled) {
        User user = getUser(userUuid);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Transactional
    public void setLocked(UUID userUuid, boolean locked) {
        User user = getUser(userUuid);
        user.setAccountNonLocked(!locked);
        if (!locked) {
            user.setFailedLoginAttempts(0);
        }
        userRepository.save(user);
    }

    private User getUser(UUID userUuid) {
        return userRepository.findByUuid(userUuid)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", userUuid));
    }
}

