package kh.edu.istad.iam.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class UserImageRedisService {

    private final RedisTemplate<String, byte[]> byteRedisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX_DATA = "user:avatar:data:";
    private static final String KEY_PREFIX_MIME = "user:avatar:mime:";
    private static final String KEY_PREFIX_USER_CURRENT = "user:avatar:current:";
    private static final Duration AVATAR_TTL = Duration.ofDays(30);

    public UserImageRedisService(
            RedisTemplate<String, byte[]> byteRedisTemplate,
            StringRedisTemplate stringRedisTemplate
    ) {
        this.byteRedisTemplate = byteRedisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Store user avatar image bytes and MIME type in Redis with fault-tolerance.
     */
    public boolean storeUserAvatar(String username, String filename, String contentType, byte[] imageBytes) {
        if (filename == null || imageBytes == null || imageBytes.length == 0) {
            return false;
        }
        try {
            String dataKey = KEY_PREFIX_DATA + filename;
            String mimeKey = KEY_PREFIX_MIME + filename;

            byteRedisTemplate.opsForValue().set(dataKey, imageBytes, AVATAR_TTL);
            stringRedisTemplate.opsForValue().set(mimeKey, contentType != null ? contentType : "image/png", AVATAR_TTL);

            if (username != null && !username.isBlank()) {
                String userKey = KEY_PREFIX_USER_CURRENT + username.toLowerCase();
                stringRedisTemplate.opsForValue().set(userKey, filename, AVATAR_TTL);
            }

            log.info("Successfully stored avatar in Redis. Filename: {}, Size: {} bytes, User: {}",
                    filename, imageBytes.length, username);
            return true;
        } catch (Exception e) {
            log.warn("Could not save avatar to Redis (fallback to disk storage active): {}", e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve avatar image bytes directly from Redis.
     */
    public Optional<byte[]> getAvatarBytes(String filename) {
        if (filename == null || filename.isBlank()) {
            return Optional.empty();
        }
        try {
            String dataKey = KEY_PREFIX_DATA + filename;
            byte[] bytes = byteRedisTemplate.opsForValue().get(dataKey);
            return Optional.ofNullable(bytes);
        } catch (Exception e) {
            log.warn("Redis fetch error for avatar {}: {}", filename, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieve avatar MIME type from Redis.
     */
    public String getAvatarMimeType(String filename) {
        if (filename == null || filename.isBlank()) {
            return "image/png";
        }
        try {
            String mimeKey = KEY_PREFIX_MIME + filename;
            String mime = stringRedisTemplate.opsForValue().get(mimeKey);
            return (mime != null && !mime.isBlank()) ? mime : "image/png";
        } catch (Exception e) {
            return "image/png";
        }
    }

    /**
     * Check if Redis is alive and connected.
     */
    public boolean isRedisAvailable() {
        try {
            var connectionFactory = stringRedisTemplate.getConnectionFactory();
            if (connectionFactory == null) return false;
            String ping = connectionFactory.getConnection().ping();
            return "PONG".equalsIgnoreCase(ping);
        } catch (Exception e) {
            return false;
        }
    }
}
