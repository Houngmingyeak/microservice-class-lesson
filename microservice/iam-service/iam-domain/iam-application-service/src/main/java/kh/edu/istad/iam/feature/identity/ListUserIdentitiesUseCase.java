package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListUserIdentitiesUseCase {

    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<IdentityResult> execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        return identityRepository.findByUserId(user.getId()).stream()
                .map(i -> new IdentityResult(
                        i.getUuid(),
                        i.getProvider(),
                        i.getEmail(),
                        i.getDisplayName(),
                        i.getLinkedAt()
                ))
                .toList();
    }
}

