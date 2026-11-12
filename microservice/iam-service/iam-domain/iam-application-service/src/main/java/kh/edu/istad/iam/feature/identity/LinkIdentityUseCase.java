package kh.edu.istad.iam.feature.identity;

import kh.edu.istad.iam.domain.model.Identity;
import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import kh.edu.istad.iam.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkIdentityUseCase {

    private final IdentityRepository identityRepository;
    private final UserRepository userRepository;

    @Transactional
    public IdentityResult execute(UUID userUuid, FederatedIdentityCommand command) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with uuid: " + userUuid));

        // Rule: One identity per provider per user
        identityRepository.findByUserId(user.getId()).stream()
                .filter(i -> i.getProvider().equalsIgnoreCase(command.provider()))
                .findFirst()
                .ifPresent(existing -> {
                    throw new IllegalStateException("User already linked to provider: " + command.provider());
                });

        Identity identity = Identity.link(
                user.getId(),
                command.provider(),
                command.providerSubject(),
                command.email(),
                command.emailVerified(),
                command.displayName(),
                command.username(),
                command.pictureUrl(),
                command.attributes()
        );
        Identity saved = identityRepository.save(identity);

        return new IdentityResult(
                saved.getUuid(),
                saved.getProvider(),
                saved.getEmail(),
                saved.getDisplayName(),
                saved.getLinkedAt()
        );
    }
}

