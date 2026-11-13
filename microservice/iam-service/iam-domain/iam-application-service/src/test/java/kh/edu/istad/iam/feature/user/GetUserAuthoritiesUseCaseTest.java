package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.*;
import kh.edu.istad.iam.feature.permission.PermissionRepository;
import kh.edu.istad.iam.feature.role.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserAuthoritiesUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;

    @Test
    @DisplayName("Domain Rule: Effective permissions = active role permissions + direct GRANT - direct DENY")
    void testEffectivePermissionsFormula() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();
        user.setId(1L);
        user.setUuid(userUuid);
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(user));

        // Role: ADMIN has permissions "user:read", "user:delete"
        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(10L);
        when(userRepository.findActiveRolesByUserId(1L)).thenReturn(List.of(userRole));

        Role adminRole = new Role();
        adminRole.setId(10L);
        adminRole.setName("ADMIN");
        adminRole.setUuid(UUID.randomUUID());
        when(roleRepository.findById(10L)).thenReturn(Optional.of(adminRole));

        RolePermission rp1 = new RolePermission();
        rp1.setRoleId(10L);
        rp1.setPermissionId(101L);

        RolePermission rp2 = new RolePermission();
        rp2.setRoleId(10L);
        rp2.setPermissionId(102L);

        when(roleRepository.findActivePermissionsByRoleId(10L)).thenReturn(List.of(rp1, rp2));

        Permission permUserRead = new Permission();
        permUserRead.setId(101L);
        permUserRead.setName("user:read");

        Permission permUserDelete = new Permission();
        permUserDelete.setId(102L);
        permUserDelete.setName("user:delete");

        when(permissionRepository.findById(101L)).thenReturn(Optional.of(permUserRead));
        when(permissionRepository.findById(102L)).thenReturn(Optional.of(permUserDelete));

        // Direct permissions:
        // GRANT "report:export"
        // DENY "user:delete"
        UserPermission upGrant = new UserPermission();
        upGrant.setUserId(1L);
        upGrant.setPermissionId(201L);
        upGrant.setEffect("GRANT");

        UserPermission upDeny = new UserPermission();
        upDeny.setUserId(1L);
        upDeny.setPermissionId(102L);
        upDeny.setEffect("DENY");

        when(userRepository.findActivePermissionsByUserId(1L)).thenReturn(List.of(upGrant, upDeny));

        Permission permReportExport = new Permission();
        permReportExport.setId(201L);
        permReportExport.setName("report:export");
        when(permissionRepository.findById(201L)).thenReturn(Optional.of(permReportExport));

        // Execute
        UserAuthoritiesResult result = getUserAuthoritiesUseCase.execute(userUuid);

        // Verification:
        // user:read (from role) -> should be present
        assertTrue(result.effectivePermissions().contains("user:read"));

        // report:export (from direct GRANT) -> should be present
        assertTrue(result.effectivePermissions().contains("report:export"));

        // user:delete (from role, but overridden by direct DENY) -> MUST NOT be present
        assertFalse(result.effectivePermissions().contains("user:delete"));
    }
}

