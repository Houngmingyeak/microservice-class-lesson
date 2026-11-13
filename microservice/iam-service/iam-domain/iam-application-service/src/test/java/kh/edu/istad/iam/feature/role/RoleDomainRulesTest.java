package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.model.Role;
import kh.edu.istad.iam.shared.CurrentActorProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleDomainRulesTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CurrentActorProvider currentActorProvider;

    @InjectMocks
    private DeleteRoleUseCase deleteRoleUseCase;

    @InjectMocks
    private DeactivateRoleUseCase deactivateRoleUseCase;

    @Test
    @DisplayName("Domain Rule: System role cannot be deleted")
    void testSystemRoleCannotBeDeleted() {
        UUID roleUuid = UUID.randomUUID();
        Role systemRole = new Role();
        systemRole.setUuid(roleUuid);
        systemRole.setName("ADMIN");
        systemRole.setIsSystem(true);

        when(roleRepository.findByUuid(roleUuid)).thenReturn(Optional.of(systemRole));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deleteRoleUseCase.execute(roleUuid);
        });

        assertEquals("System role cannot be deleted", exception.getMessage());
    }

    @Test
    @DisplayName("Domain Rule: System role cannot be deactivated")
    void testSystemRoleCannotBeDeactivated() {
        UUID roleUuid = UUID.randomUUID();
        Role systemRole = new Role();
        systemRole.setUuid(roleUuid);
        systemRole.setName("ADMIN");
        systemRole.setIsSystem(true);

        when(roleRepository.findByUuid(roleUuid)).thenReturn(Optional.of(systemRole));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deactivateRoleUseCase.execute(roleUuid);
        });

        assertEquals("System role cannot be deactivated", exception.getMessage());
    }
}

