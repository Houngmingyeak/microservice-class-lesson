package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Output port adapter: implements UserRepository with Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userPersistenceMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return userPersistenceMapper.toDomain(saved);
    }
}
