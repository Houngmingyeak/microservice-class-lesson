package kh.edu.istad.iam.feature.role;

import jakarta.validation.Valid;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final ActivateRoleUseCase activateRoleUseCase;
    private final DeactivateRoleUseCase deactivateRoleUseCase;
    private final AssignPermissionsToRoleUseCase assignPermissionsToRoleUseCase;
    private final RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('role:create') or hasRole('ADMIN')")
    public ResponseEntity<RoleResult> createRole(@Valid @RequestBody CreateRoleRequest request) {
        CreateRoleCommand command = new CreateRoleCommand(
                request.name(),
                request.description(),
                request.permissionUuids()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createRoleUseCase.execute(command));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:read') or hasRole('ADMIN')")
    public ResponseEntity<PageResponse<RoleSummary>> listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(listRolesUseCase.execute(page, size));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:read') or hasRole('ADMIN')")
    public ResponseEntity<RoleResult> getRole(@PathVariable UUID uuid) {
        return ResponseEntity.ok(getRoleUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:update') or hasRole('ADMIN')")
    public ResponseEntity<RoleResult> updateRole(
            @PathVariable UUID uuid,
            @RequestBody UpdateRoleRequest request
    ) {
        UpdateRoleCommand command = new UpdateRoleCommand(
                request.description(),
                null
        );
        return ResponseEntity.ok(updateRoleUseCase.execute(uuid, command));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:delete') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID uuid) {
        deleteRoleUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/activate")
    @PreAuthorize("hasAuthority('role:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> activateRole(@PathVariable UUID uuid) {
        activateRoleUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/deactivate")
    @PreAuthorize("hasAuthority('role:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateRole(@PathVariable UUID uuid) {
        deactivateRoleUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/permissions")
    @PreAuthorize("hasAuthority('role:assign') or hasRole('ADMIN')")
    public ResponseEntity<List<PermissionAssignmentResult>> assignPermissions(
            @PathVariable UUID uuid,
            @Valid @RequestBody AssignPermissionToRoleRequest request
    ) {
        AssignPermissionsToRoleCommand command = new AssignPermissionsToRoleCommand(
                uuid,
                request.permissionUuids()
        );
        return ResponseEntity.ok(assignPermissionsToRoleUseCase.execute(command));
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @PreAuthorize("hasAuthority('role:assign') or hasRole('ADMIN')")
    public ResponseEntity<Void> revokePermission(
            @PathVariable UUID uuid,
            @PathVariable UUID permissionUuid,
            @RequestParam(required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid, reason);
        return ResponseEntity.noContent().build();
    }
}

