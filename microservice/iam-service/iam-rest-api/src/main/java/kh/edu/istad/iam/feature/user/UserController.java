package kh.edu.istad.iam.feature.user;

import jakarta.validation.Valid;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final SetUserStatusUseCase setUserStatusUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('user:create') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> createUser(@Valid @RequestBody CreateUserCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserUseCase.execute(command));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserSummary>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(listUsersUseCase.execute(page, size, sortBy, direction));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:read') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> getUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok(getUserUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<UserResult> updateUserProfile(
            @PathVariable UUID uuid,
            @RequestBody UpdateUserProfileCommand command
    ) {
        return ResponseEntity.ok(updateUserProfileUseCase.execute(uuid, command));
    }

    @PostMapping("/{uuid}/enable")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setEnabled(uuid, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/disable")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setEnabled(uuid, false);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/lock")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> lockUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setLocked(uuid, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/unlock")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> unlockUser(@PathVariable UUID uuid) {
        setUserStatusUseCase.setLocked(uuid, false);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/password")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable UUID uuid,
            @RequestBody String newPassword
    ) {
        resetUserPasswordUseCase.execute(uuid, newPassword);
        return ResponseEntity.noContent().build();
    }
}

