package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;

/**
 * Output port: how the application layer stores and checks users.
 * Implemented by iam-persistence.
 */
public interface UserRepository {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);
}
