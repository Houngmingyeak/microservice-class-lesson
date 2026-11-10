package kh.edu.istad.iam.feature.role;

import jakarta.validation.Valid;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Role management.
 * Uses UUID as public identifier for all endpoints.
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final GetRolesUseCase getRolesUseCase;
    private final GetRoleByUuidUseCase getRoleByUuidUseCase;
    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final AssignPermissionToRoleUseCase assignPermissionToRoleUseCase;
    private final RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<RoleResponse>> getRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(getRolesUseCase.execute(pageable));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> getRoleByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(getRoleByUuidUseCase.execute(uuid));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createRoleUseCase.execute(request));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(updateRoleUseCase.execute(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable String uuid) {
        deleteRoleUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignPermission(
            @PathVariable String uuid,
            @Valid @RequestBody AssignPermissionToRoleRequest request
    ) {
        assignPermissionToRoleUseCase.execute(uuid, request.permissionUuid());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokePermission(
            @PathVariable String uuid,
            @PathVariable String permissionUuid
    ) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid);
        return ResponseEntity.noContent().build();
    }
}
