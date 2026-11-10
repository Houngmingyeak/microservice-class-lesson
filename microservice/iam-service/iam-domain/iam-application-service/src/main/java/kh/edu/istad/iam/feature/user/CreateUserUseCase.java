package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case: Register a new user account.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserResult execute(CreateUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExistsException("Username already exists: " + command.username());
        }
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException("Email already exists: " + command.email());
        }

        String passwordHash = passwordHasher.hash(command.password());

        User user = User.register(
                command.username(),
                command.email(),
                passwordHash,
                command.givenName(),
                command.familyName(),
                command.gender(),
                command.birthdate(),
                command.phoneNumber()
        );

        User saved = userRepository.save(user);

        return new UserResult(
                saved.getUuid(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getEmailVerified(),
                saved.getGivenName(),
                saved.getFamilyName(),
                saved.getGender(),
                saved.getBirthdate(),
                saved.getPhoneNumber(),
                saved.getPhoneNumberVerified(),
                saved.getPictureUrl(),
                saved.getEnabled()
        );
    }
}
