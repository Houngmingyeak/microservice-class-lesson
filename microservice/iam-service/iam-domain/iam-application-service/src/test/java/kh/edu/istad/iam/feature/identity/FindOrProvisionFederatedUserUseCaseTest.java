package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;
import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.domain.model.UserRole;
import kh.edu.istad.iam.feature.role.RoleRepository;
import kh.edu.istad.iam.feature.user.GetUserAuthoritiesUseCase;
import kh.edu.istad.iam.feature.user.UserAuthoritiesResult;
import kh.edu.istad.iam.feature.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindOrProvisionFederatedUserUseCaseTest {

    @Mock
    private IdentityRepository identityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;

    @InjectMocks
    private FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    @Test
    @DisplayName("Find existing federated identity updates last login")
    void testExistingIdentity() {
        FederatedIdentityCommand command = new FederatedIdentityCommand(
                "GOOGLE", "sub123", "user@gmail.com", true, "Google User", "guser", null, Map.of()
        );

        Identity identity = new Identity();
        identity.setId(10L);
        identity.setUserId(1L);
        identity.setProvider("GOOGLE");
        identity.setProviderSubject("sub123");

        User user = new User();
        user.setId(1L);
        user.setUuid(UUID.randomUUID());
        user.setUsername("guser");
        user.setEmail("user@gmail.com");

        when(identityRepository.findByProviderAndProviderSubject("GOOGLE", "sub123"))
                .thenReturn(Optional.of(identity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(getUserAuthoritiesUseCase.execute(any())).thenReturn(
                new UserAuthoritiesResult(
                        List.of(new UserAuthoritiesResult.RoleAuthority(UUID.randomUUID(), "USER", null)),
                        List.of(),
                        List.of("user:read")
                )
        );

        AuthUserResult result = findOrProvisionFederatedUserUseCase.execute(command);

        assertNotNull(result);
        assertEquals("guser", result.username());
        assertTrue(result.authorities().contains("ROLE_USER"));
        assertTrue(result.authorities().contains("user:read"));
        verify(identityRepository, times(1)).save(identity);
    }

    @Test
    @DisplayName("Provision new federated user and link identity")
    void testProvisionNewFederatedUser() {
        FederatedIdentityCommand command = new FederatedIdentityCommand(
                "GITHUB", "git456", "dev@github.com", true, "GitHub Dev", "gitdev", "http://avatar", Map.of()
        );

        when(identityRepository.findByProviderAndProviderSubject("GITHUB", "git456"))
                .thenReturn(Optional.empty());
        when(userRepository.findByUsernameOrEmail("dev@github.com", "dev@github.com"))
                .thenReturn(Optional.empty());
        when(userRepository.existsByUsername("gitdev")).thenReturn(false);

        User newUser = User.register("gitdev", "dev@github.com", null, "GitHub Dev", "", "other", null, null);
        newUser.setId(2L);
        newUser.setUuid(UUID.randomUUID());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(getUserAuthoritiesUseCase.execute(any())).thenReturn(
                new UserAuthoritiesResult(
                        List.of(new UserAuthoritiesResult.RoleAuthority(UUID.randomUUID(), "USER", null)),
                        List.of(),
                        List.of("user:read")
                )
        );

        AuthUserResult result = findOrProvisionFederatedUserUseCase.execute(command);

        assertNotNull(result);
        assertEquals("gitdev", result.username());
        assertTrue(result.authorities().contains("ROLE_USER"));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).saveUserRole(any(UserRole.class));
        verify(identityRepository, times(1)).save(any(Identity.class));
    }
}

