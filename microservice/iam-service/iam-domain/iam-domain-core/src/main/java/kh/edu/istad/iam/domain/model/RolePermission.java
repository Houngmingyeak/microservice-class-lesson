package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * RolePermission assignment model. Assignments are revoked, never deleted. Plain Java.
 */
@Getter
@Setter
@NoArgsConstructor
public class RolePermission {

    private Long id;
    private Long roleId;
    private Long permissionId;
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

    public static RolePermission assign(Long roleId, Long permissionId, String assignedBy, OffsetDateTime expiresAt, String reason) {
        RolePermission rp = new RolePermission();
        rp.setRoleId(roleId);
        rp.setPermissionId(permissionId);
        rp.setAssignedAt(OffsetDateTime.now());
        rp.setAssignedBy(assignedBy);
        rp.setExpiresAt(expiresAt);
        rp.setReason(reason);
        return rp;
    }
}

