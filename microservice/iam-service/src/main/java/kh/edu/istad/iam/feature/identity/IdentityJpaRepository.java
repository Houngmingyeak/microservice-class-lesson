package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityJpaRepository extends JpaRepository<Identity, Long> {

    List<Identity> findByUserId(Long userId);

    Optional<Identity> findByProviderAndProviderSubject(String provider, String providerSubject);

    Optional<Identity> findByUuid(UUID uuid);

    boolean existsByProviderAndProviderSubject(String provider, String providerSubject);
}
