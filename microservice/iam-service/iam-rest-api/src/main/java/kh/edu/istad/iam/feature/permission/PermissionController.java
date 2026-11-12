package kh.edu.istad.iam.feature.permission;

import jakarta.validation.Valid;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final GetPermissionUseCase getPermissionUseCase;
    private final ListPermissionsUseCase listPermissionsUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final ActivatePermissionUseCase activatePermissionUseCase;
    private final DeactivatePermissionUseCase deactivatePermissionUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('permission:create') or hasRole('ADMIN')")
    public ResponseEntity<PermissionResult> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        CreatePermissionCommand command = new CreatePermissionCommand(
                request.resource(),
                request.action(),
                request.description()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createPermissionUseCase.execute(command));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read') or hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PermissionResult>> listPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(listPermissionsUseCase.execute(page, size));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:read') or hasRole('ADMIN')")
    public ResponseEntity<PermissionResult> getPermission(@PathVariable UUID uuid) {
        return ResponseEntity.ok(getPermissionUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:update') or hasRole('ADMIN')")
    public ResponseEntity<PermissionResult> updatePermission(
            @PathVariable UUID uuid,
            @RequestBody UpdatePermissionRequest request
    ) {
        UpdatePermissionCommand command = new UpdatePermissionCommand(request.description());
        return ResponseEntity.ok(updatePermissionUseCase.execute(uuid, command));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:delete') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID uuid) {
        deletePermissionUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/activate")
    @PreAuthorize("hasAuthority('permission:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> activatePermission(@PathVariable UUID uuid) {
        activatePermissionUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/deactivate")
    @PreAuthorize("hasAuthority('permission:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> deactivatePermission(@PathVariable UUID uuid) {
        deactivatePermissionUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }
}

