package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Get a single user by UUID.
 */
@Service
@RequiredArgsConstructor
public class GetUserByUuidUseCase {

    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public UserResponse execute(String uuid) {
        User user = userJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", uuid));
        return CreateUserUseCase.toResponse(user);
    }
}
