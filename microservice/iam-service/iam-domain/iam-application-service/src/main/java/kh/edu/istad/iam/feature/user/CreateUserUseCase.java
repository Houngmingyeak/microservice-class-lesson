package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Register a new user account.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse execute(CreateUserRequest request) {
        if (userJpaRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }
        if (userJpaRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }

        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .givenName(request.givenName())
                .familyName(request.familyName())
                .gender(request.gender())
                .birthdate(request.birthdate())
                .phoneNumber(request.phoneNumber())
                .emailVerified(false)
                .phoneNumberVerified(false)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .isDeleted(false)
                .failedLoginAttempts(0)
                .build();

        user = userJpaRepository.save(user);
        return toResponse(user);
    }

    static UserResponse toResponse(User user) {
        return new UserResponse(
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
