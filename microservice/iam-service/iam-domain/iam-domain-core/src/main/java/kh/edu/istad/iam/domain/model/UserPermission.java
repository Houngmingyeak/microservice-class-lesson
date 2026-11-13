package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * UserPermission assignment model. Assignments are revoked, never deleted. Plain Java.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long permissionId;
    private String effect; // GRANT or DENY
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

    public static UserPermission assign(Long userId, Long permissionId, String effect, String assignedBy, OffsetDateTime expiresAt, String reason) {
        UserPermission up = new UserPermission();
        up.setUserId(userId);
        up.setPermissionId(permissionId);
        up.setEffect(effect != null ? effect.toUpperCase() : "GRANT");
        up.setAssignedAt(OffsetDateTime.now());
        up.setAssignedBy(assignedBy);
        up.setExpiresAt(expiresAt);
        up.setReason(reason);
        return up;
    }
}

