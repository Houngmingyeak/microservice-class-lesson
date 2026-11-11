package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.feature.user.PasswordHasher;

public interface PasswordHasher {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}
