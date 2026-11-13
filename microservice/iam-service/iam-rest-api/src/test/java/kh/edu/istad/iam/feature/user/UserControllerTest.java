package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.config.GlobalExceptionHandler;
import kh.edu.istad.iam.feature.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.feature.identity.UnlinkIdentityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private ListUsersUseCase listUsersUseCase;
    @Mock
    private UpdateUserProfileUseCase updateUserProfileUseCase;
    @Mock
    private EnableUserUseCase enableUserUseCase;
    @Mock
    private DisableUserUseCase disableUserUseCase;
    @Mock
    private LockUserUseCase lockUserUseCase;
    @Mock
    private UnlockUserUseCase unlockUserUseCase;
    @Mock
    private ResetUserPasswordUseCase resetUserPasswordUseCase;
    @Mock
    private ChangePasswordUseCase changePasswordUseCase;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private AssignRolesToUserUseCase assignRolesToUserUseCase;
    @Mock
    private RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    @Mock
    private GrantPermissionsToUserUseCase grantPermissionsToUserUseCase;
    @Mock
    private RevokePermissionFromUserUseCase revokePermissionFromUserUseCase;
    @Mock
    private GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    @Mock
    private ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    @Mock
    private UnlinkIdentityUseCase unlinkIdentityUseCase;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/users/{uuid}/authorities returns UserAuthoritiesResponse")
    void testGetUserAuthorities() throws Exception {
        UUID userUuid = UUID.randomUUID();
        UserAuthoritiesResult result = new UserAuthoritiesResult(
                List.of(new UserAuthoritiesResult.RoleAuthority(UUID.randomUUID(), "ADMIN", null)),
                List.of(new UserAuthoritiesResult.DirectPermission("report:read", "GRANT")),
                List.of("user:read", "user:update", "report:read")
        );

        when(getUserAuthoritiesUseCase.execute(userUuid)).thenReturn(result);

        mockMvc.perform(get("/api/users/{uuid}/authorities", userUuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0].name").value("ADMIN"))
                .andExpect(jsonPath("$.directPermissions[0].name").value("report:read"))
                .andExpect(jsonPath("$.effectivePermissions[0]").value("user:read"));
    }

    @Test
    @DisplayName("POST /api/users/{uuid}/enable invokes EnableUserUseCase")
    void testEnableUser() throws Exception {
        UUID userUuid = UUID.randomUUID();

        mockMvc.perform(post("/api/users/{uuid}/enable", userUuid))
                .andExpect(status().isNoContent());

        verify(enableUserUseCase, times(1)).execute(userUuid);
    }

    @Test
    @DisplayName("POST /api/users/{uuid}/disable invokes DisableUserUseCase")
    void testDisableUser() throws Exception {
        UUID userUuid = UUID.randomUUID();

        mockMvc.perform(post("/api/users/{uuid}/disable", userUuid))
                .andExpect(status().isNoContent());

        verify(disableUserUseCase, times(1)).execute(userUuid);
    }

    @Test
    @DisplayName("POST /api/users/{uuid}/lock invokes LockUserUseCase")
    void testLockUser() throws Exception {
        UUID userUuid = UUID.randomUUID();

        mockMvc.perform(post("/api/users/{uuid}/lock", userUuid))
                .andExpect(status().isNoContent());

        verify(lockUserUseCase, times(1)).execute(userUuid);
    }

    @Test
    @DisplayName("POST /api/users/{uuid}/unlock invokes UnlockUserUseCase")
    void testUnlockUser() throws Exception {
        UUID userUuid = UUID.randomUUID();

        mockMvc.perform(post("/api/users/{uuid}/unlock", userUuid))
                .andExpect(status().isNoContent());

        verify(unlockUserUseCase, times(1)).execute(userUuid);
    }
}
