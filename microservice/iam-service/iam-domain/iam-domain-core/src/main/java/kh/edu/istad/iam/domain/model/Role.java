package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Role aggregate. Plain Java, no JPA, no Spring.
 */
@Getter
@Setter
@NoArgsConstructor
public class Role extends AuditableModel {

    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private Boolean isSystem;
    private Boolean isActive;
    private Boolean isDeleted;
    private OffsetDateTime deletedAt;
    private String deletedBy;
    private Long version;

    public static Role create(String name, String description, boolean isSystem) {
        Role role = new Role();
        role.setUuid(UUID.randomUUID());
        role.setName(name);
        role.setDescription(description);
        role.setIsSystem(isSystem);
        role.setIsActive(true);
        role.setIsDeleted(false);
        return role;
    }
}

