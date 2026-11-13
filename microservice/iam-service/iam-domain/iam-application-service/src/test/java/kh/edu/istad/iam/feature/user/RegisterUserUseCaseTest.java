package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.feature.role.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Register user succeeds and assigns default USER role")
    void testRegisterUserSuccess() {
        RegisterUserCommand command = new RegisterUserCommand(
                "sokdara",
                "dara.sok@istad.co",
                "Str0ng!Pass2026",
                "Str0ng!Pass2026",
                "Dara",
                "Sok",
                "km"
        );

        when(userRepository.existsByUsername("sokdara")).thenReturn(false);
        when(userRepository.existsByEmail("dara.sok@istad.co")).thenReturn(false);
        when(passwordHasher.hash("Str0ng!Pass2026")).thenReturn("hashed_pass");

        User savedUser = User.register("sokdara", "dara.sok@istad.co", "hashed_pass", "Dara", "Sok", null, null, null);
        savedUser.setId(1L);
        savedUser.setUuid(UUID.randomUUID());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        UserResult result = registerUserUseCase.execute(command);

        assertNotNull(result);
        assertEquals("sokdara", result.username());
        verify(userRepository, times(1)).saveUserRole(any(UserRole.class));
    }

    @Test
    @DisplayName("Register user fails when passwords do not match")
    void testRegisterUserPasswordMismatch() {
        RegisterUserCommand command = new RegisterUserCommand(
                "sokdara", "dara.sok@istad.co", "Pass1", "Pass2", "Dara", "Sok", "km"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            registerUserUseCase.execute(command);
        });

        assertEquals("Passwords do not match", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}

