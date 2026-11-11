package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.feature.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserResult execute(RegisterUserCommand command) {
        if (!command.password().equals(command.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("Username already exists: " + command.username());
        }
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }

        String passwordHash = passwordHasher.hash(command.password());

        User user = User.register(
                command.username(),
                command.email(),
                passwordHash,
                command.givenName(),
                command.familyName(),
                null,
                null,
                null
        );
        user.setLocale(command.locale() != null ? command.locale() : "en");

        User saved = userRepository.save(user);

        // Assign default USER role
        roleRepository.findByName("USER").ifPresent(role -> {
            userRepository.saveUserRole(UserRole.assign(saved.getId(), role.getId(), "system", null, "Self-registration"));
        });

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

