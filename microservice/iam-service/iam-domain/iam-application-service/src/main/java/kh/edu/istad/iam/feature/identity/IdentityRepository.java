package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: Persistence operations for Identity.
 * Implemented by iam-persistence.
 */
public interface IdentityRepository {

    Identity save(Identity identity);

    Optional<Identity> findByUuid(UUID uuid);

    Optional<Identity> findByProviderAndProviderSubject(String provider, String providerSubject);

    List<Identity> findByUserId(Long userId);

    long countByUserId(Long userId);

    void delete(Identity identity);
}

