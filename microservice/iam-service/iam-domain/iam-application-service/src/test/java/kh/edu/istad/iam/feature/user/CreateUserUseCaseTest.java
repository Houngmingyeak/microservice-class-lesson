package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("Create user succeeds and hashes password")
    void testCreateUserSuccess() {
        CreateUserCommand command = new CreateUserCommand(
                "testuser",
                "test@example.com",
                "Password123!",
                "Test",
                "User",
                "male",
                LocalDate.of(2000, 1, 1),
                "+85512345678"
        );

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordHasher.hash("Password123!")).thenReturn("hashed_secret");

        User savedUser = User.register(
                command.username(),
                command.email(),
                "hashed_secret",
                command.givenName(),
                command.familyName(),
                command.gender(),
                command.birthdate(),
                command.phoneNumber()
        );
        savedUser.setId(1L);
        savedUser.setUuid(UUID.randomUUID());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResult result = createUserUseCase.execute(command);

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("test@example.com", result.email());
        verify(passwordHasher, times(1)).hash("Password123!");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Create user fails if username exists")
    void testCreateUserDuplicateUsername() {
        CreateUserCommand command = new CreateUserCommand(
                "existing", "test@example.com", "Password123!", "T", "U", "male", null, null
        );

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            createUserUseCase.execute(command);
        });

        verify(userRepository, never()).save(any());
    }
}

