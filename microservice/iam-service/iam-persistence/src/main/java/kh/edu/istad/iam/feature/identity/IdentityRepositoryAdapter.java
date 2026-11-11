package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.Identity;
import kh.edu.istad.iam.feature.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentityRepositoryAdapter implements IdentityRepository {

    private final IdentityJpaRepository identityJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public kh.edu.istad.iam.domain.model.Identity save(kh.edu.istad.iam.domain.model.Identity identity) {
        Identity entity = toEntity(identity);
        Identity saved = identityJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Identity> findByUuid(UUID uuid) {
        return identityJpaRepository.findByUuid(uuid).map(this::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Identity> findByProviderAndProviderSubject(String provider, String providerSubject) {
        return identityJpaRepository.findByProviderAndProviderSubject(provider, providerSubject).map(this::toDomain);
    }

    @Override
    public List<kh.edu.istad.iam.domain.model.Identity> findByUserId(Long userId) {
        return identityJpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public long countByUserId(Long userId) {
        return identityJpaRepository.findByUserId(userId).size();
    }

    @Override
    public void delete(kh.edu.istad.iam.domain.model.Identity identity) {
        identityJpaRepository.findById(identity.getId()).ifPresent(identityJpaRepository::delete);
    }

    private kh.edu.istad.iam.domain.model.Identity toDomain(Identity entity) {
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

    private Identity toEntity(kh.edu.istad.iam.domain.model.Identity model) {
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

