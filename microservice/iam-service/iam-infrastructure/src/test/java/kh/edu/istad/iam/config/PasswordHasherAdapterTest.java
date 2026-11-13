package kh.edu.istad.iam.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherAdapterTest {

    private final PasswordHasherAdapter hasher = new PasswordHasherAdapter(new BCryptPasswordEncoder());

    @Test
    @DisplayName("PasswordHasherAdapter correctly hashes and matches raw passwords")
    void testHashAndMatch() {
        String raw = "Str0ng!Pass2026";
        String hashed = hasher.hash(raw);

        assertNotEquals(raw, hashed);
        assertTrue(hasher.matches(raw, hashed));
    }
}

