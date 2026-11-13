package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.feature.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnlinkIdentityUseCaseTest {

    @Mock
    private IdentityRepository identityRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UnlinkIdentityUseCase unlinkIdentityUseCase;

    @Test
    @DisplayName("Domain Rule: Cannot unlink last sign-in method when user has no password")
    void testCannotUnlinkLastIdentityWithoutPassword() {
        UUID userUuid = UUID.randomUUID();
        UUID identityUuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);
        user.setUuid(userUuid);
        user.setPasswordHash(null); // No password set

        Identity identity = new Identity();
        identity.setId(10L);
        identity.setUuid(identityUuid);
        identity.setUserId(1L);

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(user));
        when(identityRepository.findByUuid(identityUuid)).thenReturn(Optional.of(identity));
        when(identityRepository.countByUserId(1L)).thenReturn(1L); // Only 1 identity

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            unlinkIdentityUseCase.execute(userUuid, identityUuid);
        });

        assertEquals("Cannot unlink the last sign-in method", ex.getMessage());
        verify(identityRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Domain Rule: Can unlink identity when user has password set")
    void testCanUnlinkWhenUserHasPassword() {
        UUID userUuid = UUID.randomUUID();
        UUID identityUuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);
        user.setUuid(userUuid);
        user.setPasswordHash("hashed_password_123");

        Identity identity = new Identity();
        identity.setId(10L);
        identity.setUuid(identityUuid);
        identity.setUserId(1L);

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(user));
        when(identityRepository.findByUuid(identityUuid)).thenReturn(Optional.of(identity));
        when(identityRepository.countByUserId(1L)).thenReturn(1L);

        assertDoesNotThrow(() -> {
            unlinkIdentityUseCase.execute(userUuid, identityUuid);
        });

        verify(identityRepository, times(1)).delete(identity);
    }
}

