package kh.edu.istad.iam.feature.permission;

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
 * REST controller for Permission management.
 * Uses UUID as public identifier.
 */
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final GetPermissionsUseCase getPermissionsUseCase;
    private final GetPermissionByUuidUseCase getPermissionByUuidUseCase;
    private final CreatePermissionUseCase createPermissionUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PermissionResponse>> getPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(getPermissionsUseCase.execute(pageable));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> getPermissionByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(getPermissionByUuidUseCase.execute(uuid));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody CreatePermissionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createPermissionUseCase.execute(request));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable String uuid,
            @Valid @RequestBody UpdatePermissionRequest request
    ) {
        return ResponseEntity.ok(updatePermissionUseCase.execute(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable String uuid) {
        deletePermissionUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }
}
