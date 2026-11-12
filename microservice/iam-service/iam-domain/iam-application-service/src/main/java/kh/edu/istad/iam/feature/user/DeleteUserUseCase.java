package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        user.setIsDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());
        user.setDeletedBy(currentActorProvider.getCurrentActorSub());
        userRepository.save(user);
    }
}

