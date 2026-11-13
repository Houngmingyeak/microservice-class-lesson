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
    private final IdentityPersistenceMapper identityPersistenceMapper;

    @Override
    public kh.edu.istad.iam.domain.model.Identity save(kh.edu.istad.iam.domain.model.Identity identity) {
        Identity entity = identityPersistenceMapper.toEntity(identity);
        Identity saved = identityJpaRepository.save(entity);
        return identityPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Identity> findByUuid(UUID uuid) {
        return identityJpaRepository.findByUuid(uuid).map(identityPersistenceMapper::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Identity> findByProviderAndProviderSubject(String provider, String providerSubject) {
        return identityJpaRepository.findByProviderAndProviderSubject(provider, providerSubject).map(identityPersistenceMapper::toDomain);
    }

    @Override
    public List<kh.edu.istad.iam.domain.model.Identity> findByUserId(Long userId) {
        return identityJpaRepository.findByUserId(userId).stream().map(identityPersistenceMapper::toDomain).toList();
    }

    @Override
    public long countByUserId(Long userId) {
        return identityJpaRepository.findByUserId(userId).size();
    }

    @Override
    public void delete(kh.edu.istad.iam.domain.model.Identity identity) {
        identityJpaRepository.findById(identity.getId()).ifPresent(identityJpaRepository::delete);
    }
}

