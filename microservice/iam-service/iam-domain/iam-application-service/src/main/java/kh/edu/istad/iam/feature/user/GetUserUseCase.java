package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResult execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", userUuid));

        return new UserResult(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getEmailVerified(),
                user.getGivenName(),
                user.getFamilyName(),
                user.getGender(),
                user.getBirthdate(),
                user.getPhoneNumber(),
                user.getPhoneNumberVerified(),
                user.getPictureUrl(),
                user.getEnabled()
        );
    }
}

