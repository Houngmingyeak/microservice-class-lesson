package kh.edu.istad.iam.feature.user;

import jakarta.validation.Valid;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.identity.IdentityResult;
import kh.edu.istad.iam.feature.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.feature.identity.UnlinkIdentityUseCase;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final SetUserStatusUseCase setUserStatusUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final AssignRolesToUserUseCase assignRolesToUserUseCase;
    private final RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    private final GrantPermissionsToUserUseCase grantPermissionsToUserUseCase;
    private final RevokePermissionFromUserUseCase revokePermissionFromUserUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UserRepository userRepository;

    // --- Users endpoints ---

    @PostMapping("/api/users")
    @PreAuthorize("hasAuthority('user:create') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> createUser(@Valid @RequestBody CreateUserCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserUseCase.execute(command));
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserSummary>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(listUsersUseCase.execute(page, size, sortBy, direction));
    }

    @GetMapping("/api/users/{uuid}")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> getUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok(getUserUseCase.execute(uuid));
    }

    @PatchMapping("/api/users/{uuid}")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> updateUserProfile(
            @PathVariable UUID uuid,
            @RequestBody UpdateUserProfileCommand command
    ) {
        return ResponseEntity.ok(updateUserProfileUseCase.execute(uuid, command));
    }

    @DeleteMapping("/api/users/{uuid}")
    @PreAuthorize("hasAuthority('user:delete') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        deleteUserUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/users/{uuid}/enable")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setEnabled(uuid, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/users/{uuid}/disable")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setEnabled(uuid, false);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/users/{uuid}/lock")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> lockUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setLocked(uuid, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/users/{uuid}/unlock")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> unlockUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setLocked(uuid, false);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/users/{uuid}/password")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable UUID uuid,
            @RequestBody String newPassword
    ) {
        resetUserPasswordUseCase.execute(uuid, newPassword);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/users/{uuid}/authorities")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<UserAuthoritiesResult> getUserAuthorities(@PathVariable UUID uuid) {
        return ResponseEntity.ok(getUserAuthoritiesUseCase.execute(uuid));
    }

    @PostMapping("/api/users/{uuid}/roles")
    @PreAuthorize("hasAuthority('user:assign') or hasRole('ADMIN')")
    public ResponseEntity<List<RoleAssignmentResult>> assignRoles(
            @PathVariable UUID uuid,
            @Valid @RequestBody AssignRolesToUserRequest request
    ) {
        AssignRolesToUserCommand command = new AssignRolesToUserCommand(
                request.roleUuids(),
                request.expiresAt(),
                request.reason()
        );
        return ResponseEntity.ok(assignRolesToUserUseCase.execute(uuid, command));
    }

    @DeleteMapping("/api/users/{uuid}/roles/{roleUuid}")
    @PreAuthorize("hasAuthority('user:assign') or hasRole('ADMIN')")
    public ResponseEntity<Void> revokeRole(
            @PathVariable UUID uuid,
            @PathVariable UUID roleUuid,
            @RequestParam(required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid, reason);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/users/{uuid}/permissions")
    @PreAuthorize("hasAuthority('user:assign') or hasRole('ADMIN')")
    public ResponseEntity<List<PermissionGrantResult>> grantPermissions(
            @PathVariable UUID uuid,
            @Valid @RequestBody GrantPermissionsToUserRequest request
    ) {
        GrantPermissionsToUserCommand command = new GrantPermissionsToUserCommand(
                request.permissionUuids(),
                request.effect(),
                request.expiresAt(),
                request.reason()
        );
        return ResponseEntity.ok(grantPermissionsToUserUseCase.execute(uuid, command));
    }

    @DeleteMapping("/api/users/{uuid}/permissions/{permissionUuid}")
    @PreAuthorize("hasAuthority('user:assign') or hasRole('ADMIN')")
    public ResponseEntity<Void> revokePermission(
            @PathVariable UUID uuid,
            @PathVariable UUID permissionUuid,
            @RequestParam(required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokePermissionFromUserUseCase.execute(uuid, permissionUuid, reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/users/{uuid}/identities")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<List<IdentityResult>> listUserIdentities(@PathVariable UUID uuid) {
        return ResponseEntity.ok(listUserIdentitiesUseCase.execute(uuid));
    }

    @DeleteMapping("/api/users/{uuid}/identities/{identityUuid}")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> unlinkUserIdentity(
            @PathVariable UUID uuid,
            @PathVariable UUID identityUuid
    ) {
        unlinkIdentityUseCase.execute(uuid, identityUuid);
        return ResponseEntity.noContent().build();
    }

    // --- /api/me endpoints (authenticated user) ---

    @GetMapping("/api/me")
    public ResponseEntity<UserResult> getMe(Authentication authentication) {
        UUID currentUuid = resolveCurrentUserUuid(authentication);
        return ResponseEntity.ok(getUserUseCase.execute(currentUuid));
    }

    @PatchMapping("/api/me")
    public ResponseEntity<UserResult> updateMe(
            Authentication authentication,
            @RequestBody UpdateUserProfileCommand command
    ) {
        UUID currentUuid = resolveCurrentUserUuid(authentication);
        return ResponseEntity.ok(updateUserProfileUseCase.execute(currentUuid, command));
    }

    @PutMapping("/api/me/password")
    public ResponseEntity<Void> changeMePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UUID currentUuid = resolveCurrentUserUuid(authentication);
        changePasswordUseCase.execute(
                currentUuid,
                request.currentPassword(),
                request.newPassword(),
                request.confirmPassword() != null ? request.confirmPassword() : request.newPassword()
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/me/identities")
    public ResponseEntity<List<IdentityResult>> getMeIdentities(Authentication authentication) {
        UUID currentUuid = resolveCurrentUserUuid(authentication);
        return ResponseEntity.ok(listUserIdentitiesUseCase.execute(currentUuid));
    }

    @DeleteMapping("/api/me/identities/{identityUuid}")
    public ResponseEntity<Void> unlinkMeIdentity(
            Authentication authentication,
            @PathVariable UUID identityUuid
    ) {
        UUID currentUuid = resolveCurrentUserUuid(authentication);
        unlinkIdentityUseCase.execute(currentUuid, identityUuid);
        return ResponseEntity.noContent().build();
    }

    private UUID resolveCurrentUserUuid(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .map(kh.edu.istad.iam.domain.model.User::getUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for authenticated principal: " + username));
    }
}
