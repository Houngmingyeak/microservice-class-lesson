package kh.edu.istad.iam.feature.user;

/**
 * Output port: how the application layer hashes passwords.
 * Implemented by iam-infrastructure.
 */
public interface PasswordHasher {

    String hash(String rawPassword);
}
