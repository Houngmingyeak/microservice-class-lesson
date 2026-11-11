package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * UserRole assignment model. Assignments are revoked, never deleted. Plain Java.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    private Long id;
    private Long userId;
    private Long roleId;
    private OffsetDateTime assignedAt;
    private String assignedBy;
    private OffsetDateTime expiresAt;
    private OffsetDateTime revokedAt;
    private String revokedBy;
    private String reason;

    public boolean isActive() {
        if (revokedAt != null) {
            return false;
        }
        return expiresAt == null || expiresAt.isAfter(OffsetDateTime.now());
    }

    public static UserRole assign(Long userId, Long roleId, String assignedBy, OffsetDateTime expiresAt, String reason) {
        UserRole ur = new UserRole();
        ur.setUserId(userId);
        ur.setRoleId(roleId);
        ur.setAssignedAt(OffsetDateTime.now());
        ur.setAssignedBy(assignedBy);
        ur.setExpiresAt(expiresAt);
        ur.setReason(reason);
        return ur;
    }
}

