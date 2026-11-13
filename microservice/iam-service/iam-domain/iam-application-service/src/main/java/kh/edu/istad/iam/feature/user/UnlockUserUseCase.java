package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnlockUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));
        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }
}

