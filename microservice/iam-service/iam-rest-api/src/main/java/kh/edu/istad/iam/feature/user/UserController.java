package kh.edu.istad.iam.feature.user;

import jakarta.validation.Valid;
import kh.edu.istad.iam.feature.role.RoleResponse;
import kh.edu.istad.iam.feature.userrole.AssignRoleRequest;
import kh.edu.istad.iam.feature.userrole.AssignRoleToUserUseCase;
import kh.edu.istad.iam.feature.userrole.GetUserRolesUseCase;
import kh.edu.istad.iam.feature.userrole.RevokeRoleFromUserUseCase;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUsersUseCase getUsersUseCase;
    private final GetUserByUuidUseCase getUserByUuidUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final AssignRoleToUserUseCase assignRoleToUserUseCase;
    private final RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    private final GetUserRolesUseCase getUserRolesUseCase;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(getUsersUseCase.execute(pageable));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(getUserByUuidUseCase.execute(uuid));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserUseCase.execute(request));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String uuid,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(updateUserUseCase.execute(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:delete') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        deleteUserUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/password")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> changePassword(
            @PathVariable String uuid,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        changePasswordUseCase.execute(uuid, request);
        return ResponseEntity.noContent().build();
    }

    // ---- User Role Endpoints ----

    @PostMapping("/{uuid}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRole(
            @PathVariable String uuid,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        assignRoleToUserUseCase.execute(uuid, request.roleUuid());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{uuid}/roles/{roleUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeRole(
            @PathVariable String uuid,
            @PathVariable String roleUuid
    ) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/roles")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponse>> getUserRoles(@PathVariable String uuid) {
        return ResponseEntity.ok(getUserRolesUseCase.execute(uuid));
    }
}
