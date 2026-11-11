package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Permission aggregate. Plain Java, no JPA, no Spring.
 */
@Getter
@Setter
@NoArgsConstructor
public class Permission extends AuditableModel {

    private Long id;
    private UUID uuid;
    private String name;
    private String resource;
    private String action;
    private String description;
    private Boolean isSystem;
    private Boolean isActive;
    private Boolean isDeleted;
    private OffsetDateTime deletedAt;
    private String deletedBy;
    private Long version;

    public static Permission create(String resource, String action, String description, boolean isSystem) {
        Permission permission = new Permission();
        permission.setUuid(UUID.randomUUID());
        permission.setResource(resource);
        permission.setAction(action);
        permission.setName(resource + ":" + action);
        permission.setDescription(description);
        permission.setIsSystem(isSystem);
        permission.setIsActive(true);
        permission.setIsDeleted(false);
        return permission;
    }
}

