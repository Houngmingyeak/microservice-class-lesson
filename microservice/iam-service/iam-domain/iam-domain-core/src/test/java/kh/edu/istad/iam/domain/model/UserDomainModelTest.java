package kh.edu.istad.iam.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserDomainModelTest {

    @Test
    @DisplayName("User.register creates enabled user with non-expired account and credentials")
    void testRegisterUser() {
        User user = User.register(
                "sokdara",
                "dara.sok@istad.co",
                "hashed_pass",
                "Dara",
                "Sok",
                "male",
                LocalDate.of(2000, 5, 20),
                "+85512345678"
        );

        assertNotNull(user);
        assertNotNull(user.getUuid());
        assertEquals("sokdara", user.getUsername());
        assertEquals("dara.sok@istad.co", user.getEmail());
        assertTrue(user.getEnabled());
        assertTrue(user.getAccountNonExpired());
        assertTrue(user.getAccountNonLocked());
        assertTrue(user.getCredentialsNonExpired());
        assertFalse(user.getIsDeleted());
    }

    @Test
    @DisplayName("Role.create creates active role with non-deleted status")
    void testCreateRole() {
        Role role = Role.create("MANAGER", "Store manager", false);

        assertNotNull(role);
        assertNotNull(role.getUuid());
        assertEquals("MANAGER", role.getName());
        assertEquals("Store manager", role.getDescription());
        assertFalse(role.getIsSystem());
        assertTrue(role.getIsActive());
        assertFalse(role.getIsDeleted());
    }

    @Test
    @DisplayName("Identity.link creates social identity linked to user")
    void testLinkIdentity() {
        Identity identity = Identity.link(
                1L,
                "GOOGLE",
                "sub-999",
                "dara.sok@gmail.com",
                true,
                "Dara Sok",
                "darasok",
                "https://avatar.png",
                java.util.Map.of()
        );

        assertNotNull(identity);
        assertNotNull(identity.getUuid());
        assertEquals(1L, identity.getUserId());
        assertEquals("GOOGLE", identity.getProvider());
        assertEquals("sub-999", identity.getProviderSubject());
        assertTrue(identity.getEmailVerified());
        assertNotNull(identity.getLinkedAt());
    }
}

