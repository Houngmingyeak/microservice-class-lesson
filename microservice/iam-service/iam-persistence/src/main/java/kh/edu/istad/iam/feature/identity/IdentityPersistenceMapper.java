package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.Identity;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentityPersistenceMapper {

    private final UserJpaRepository userJpaRepository;

    public kh.edu.istad.iam.domain.model.Identity toDomain(Identity entity) {
        if (entity == null) return null;
        kh.edu.istad.iam.domain.model.Identity model = new kh.edu.istad.iam.domain.model.Identity();
        model.setId(entity.getId());
        model.setUuid(entity.getUuid());
        model.setUserId(entity.getUser().getId());
        model.setProvider(entity.getProvider());
        model.setProviderSubject(entity.getProviderSubject());
        model.setEmail(entity.getEmail());
        model.setEmailVerified(entity.getEmailVerified());
        model.setDisplayName(entity.getDisplayName());
        model.setUsername(entity.getUsername());
        model.setPictureUrl(entity.getPictureUrl());
        model.setAttributes(entity.getAttributes());
        model.setLinkedAt(entity.getLinkedAt());
        model.setLastLoginAt(entity.getLastLoginAt());
        model.setIsDeleted(entity.getIsDeleted());
        model.setDeletedAt(entity.getDeletedAt());
        model.setDeletedBy(entity.getDeletedBy());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setVersion(entity.getVersion());
        return model;
    }

    public Identity toEntity(kh.edu.istad.iam.domain.model.Identity model) {
        if (model == null) return null;
        var userEntity = userJpaRepository.findById(model.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + model.getUserId()));

        Identity entity = new Identity();
        entity.setId(model.getId());
        entity.setUuid(model.getUuid());
        entity.setUser(userEntity);
        entity.setProvider(model.getProvider());
        entity.setProviderSubject(model.getProviderSubject());
        entity.setEmail(model.getEmail());
        entity.setEmailVerified(model.getEmailVerified());
        entity.setDisplayName(model.getDisplayName());
        entity.setUsername(model.getUsername());
        entity.setPictureUrl(model.getPictureUrl());
        entity.setAttributes(model.getAttributes());
        entity.setLinkedAt(model.getLinkedAt());
        entity.setLastLoginAt(model.getLastLoginAt());
        entity.setIsDeleted(model.getIsDeleted());
        entity.setDeletedAt(model.getDeletedAt());
        entity.setDeletedBy(model.getDeletedBy());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setVersion(model.getVersion());
        return entity;
    }
}

