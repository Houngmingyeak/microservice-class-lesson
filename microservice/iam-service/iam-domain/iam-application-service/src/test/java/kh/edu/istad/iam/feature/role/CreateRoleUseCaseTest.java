package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private CurrentActorProvider currentActorProvider;

    @InjectMocks
    private CreateRoleUseCase createRoleUseCase;

    @Test
    @DisplayName("Create role succeeds when name is unique")
    void testCreateRoleSuccess() {
        CreateRoleCommand command = new CreateRoleCommand(
                "CONTENT_MANAGER",
                "Manages course content",
                List.of()
        );

        when(roleRepository.existsByName("CONTENT_MANAGER")).thenReturn(false);

        Role saved = Role.create("CONTENT_MANAGER", "Manages course content", false);
        saved.setId(5L);
        saved.setUuid(UUID.randomUUID());
        when(roleRepository.save(any(Role.class))).thenReturn(saved);

        RoleResult result = createRoleUseCase.execute(command);

        assertNotNull(result);
        assertEquals("CONTENT_MANAGER", result.name());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Create role fails when name already exists")
    void testCreateRoleDuplicateName() {
        CreateRoleCommand command = new CreateRoleCommand(
                "ADMIN", "Administrator", List.of()
        );

        when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            createRoleUseCase.execute(command);
        });

        assertTrue(ex.getMessage().contains("already exists"));
        verify(roleRepository, never()).save(any());
    }
}

