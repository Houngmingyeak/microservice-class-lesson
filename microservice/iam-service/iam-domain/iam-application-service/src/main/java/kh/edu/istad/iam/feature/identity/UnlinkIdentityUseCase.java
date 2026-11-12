package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnlinkIdentityUseCase {

    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(UUID userUuid, UUID identityUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        Identity identity = identityRepository.findByUuid(identityUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Identity not found with uuid: " + identityUuid));

        if (!identity.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("Identity does not belong to this user");
        }

        // Rule: a user cannot unlink their last sign-in method
        long identityCount = identityRepository.countByUserId(user.getId());
        boolean hasPassword = user.getPasswordHash() != null && !user.getPasswordHash().isBlank();

        if (identityCount <= 1 && !hasPassword) {
            throw new IllegalStateException("Cannot unlink the last sign-in method");
        }

        identityRepository.delete(identity);
    }
}

