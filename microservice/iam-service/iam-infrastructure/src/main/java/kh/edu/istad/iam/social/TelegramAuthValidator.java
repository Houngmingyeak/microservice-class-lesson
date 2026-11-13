package kh.edu.istad.iam.social;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

/**
 * Validates Telegram Login Widget and WebApp authentication hashes
 * using HMAC-SHA256 with the bot token.
 */
@Slf4j
@Component
public class TelegramAuthValidator {

    @Value("${telegram.bot-token:}")
    private String botToken;

    public boolean validate(Map<String, String> params) {
        String hash = params.get("hash");
        if (hash == null || botToken == null || botToken.isBlank()) {
            log.warn("Telegram validation failed: hash missing or botToken not configured");
            return false;
        }

        try {
            // Check auth_date not older than 24 hours (86400 seconds)
            String authDateStr = params.get("auth_date");
            if (authDateStr != null) {
                long authDate = Long.parseLong(authDateStr);
                long now = Instant.now().getEpochSecond();
                if (now - authDate > 86400) {
                    log.warn("Telegram auth expired: auth_date={} now={}", authDate, now);
                    return false;
                }
            }

            // Build data-check-string with sorted parameters excluding "hash"
            Map<String, String> sorted = new TreeMap<>(params);
            sorted.remove("hash");

            StringBuilder checkString = new StringBuilder();
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                if (checkString.length() > 0) {
                    checkString.append("\n");
                }
                checkString.append(entry.getKey()).append("=").append(entry.getValue());
            }

            // Secret key = SHA256(botToken)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = digest.digest(botToken.getBytes(StandardCharsets.UTF_8));

            // HMAC-SHA256(dataCheckString, secretKey)
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hmac = mac.doFinal(checkString.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hmac) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString().equalsIgnoreCase(hash);
        } catch (Exception e) {
            log.error("Failed to validate Telegram auth data", e);
            return false;
        }
    }
}

