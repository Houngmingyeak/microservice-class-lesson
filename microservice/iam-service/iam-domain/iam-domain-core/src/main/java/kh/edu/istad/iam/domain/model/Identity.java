package kh.edu.istad.iam.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Federated Identity model (Google, GitHub, Facebook, Telegram). Plain Java.
 */
@Getter
@Setter
@NoArgsConstructor
public class Identity extends AuditableModel {

    private Long id;
    private UUID uuid;
    private Long userId;
    private String provider;
    private String providerSubject;
    private String email;
    private Boolean emailVerified;
    private String displayName;
    private String username;
    private String pictureUrl;
    private Map<String, Object> attributes;
    private OffsetDateTime linkedAt;
    private OffsetDateTime lastLoginAt;
    private Boolean isDeleted;
    private OffsetDateTime deletedAt;
    private String deletedBy;
    private Long version;

    public static Identity link(
            Long userId,
            String provider,
            String providerSubject,
            String email,
            Boolean emailVerified,
            String displayName,
            String username,
            String pictureUrl,
            Map<String, Object> attributes
    ) {
        Identity identity = new Identity();
        identity.setUuid(UUID.randomUUID());
        identity.setUserId(userId);
        identity.setProvider(provider);
        identity.setProviderSubject(providerSubject);
        identity.setEmail(email);
        identity.setEmailVerified(emailVerified != null ? emailVerified : false);
        identity.setDisplayName(displayName);
        identity.setUsername(username);
        identity.setPictureUrl(pictureUrl);
        identity.setAttributes(attributes);
        identity.setLinkedAt(OffsetDateTime.now());
        identity.setIsDeleted(false);
        return identity;
    }
}

