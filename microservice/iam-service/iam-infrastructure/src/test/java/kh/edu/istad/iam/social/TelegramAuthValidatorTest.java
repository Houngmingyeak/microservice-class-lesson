package kh.edu.istad.iam.social;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TelegramAuthValidatorTest {

    @Test
    @DisplayName("TelegramAuthValidator verifies valid HMAC-SHA256 hash")
    void testValidTelegramHash() throws Exception {
        TelegramAuthValidator validator = new TelegramAuthValidator();
        String botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11";
        ReflectionTestUtils.setField(validator, "botToken", botToken);

        long authDate = Instant.now().getEpochSecond();
        String checkString = "auth_date=" + authDate + "\nfirst_name=Dara\nid=987654321\nusername=darasok";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] secretKey = digest.digest(botToken.getBytes(StandardCharsets.UTF_8));

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
        byte[] hmac = mac.doFinal(checkString.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : hmac) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1) hex.append('0');
            hex.append(h);
        }

        Map<String, String> params = new HashMap<>();
        params.put("id", "987654321");
        params.put("first_name", "Dara");
        params.put("username", "darasok");
        params.put("auth_date", String.valueOf(authDate));
        params.put("hash", hex.toString());

        assertTrue(validator.validate(params));
    }

    @Test
    @DisplayName("TelegramAuthValidator rejects expired or invalid hash")
    void testInvalidTelegramHash() {
        TelegramAuthValidator validator = new TelegramAuthValidator();
        ReflectionTestUtils.setField(validator, "botToken", "bot_token_123");

        Map<String, String> params = Map.of(
                "id", "123",
                "auth_date", String.valueOf(Instant.now().getEpochSecond() - 90000), // > 24 hours
                "hash", "wrong_hash"
        );

        assertFalse(validator.validate(params));
    }
}

