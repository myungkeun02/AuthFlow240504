package org.myungkeun.auth_flow.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor

public class CacheManager {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void save(@NonNull final String key, @NonNull final Object value, @NonNull final Duration timeToLive) {
        redisTemplate.opsForValue().set(key, value, timeToLive);
    }

    public void save(@NonNull final String key, @NonNull final Duration timeToLive) {
        redisTemplate.opsForValue().set(key, "", timeToLive);
    }

    public Boolean isPresent(@NonNull final String key) {
        final var fetchedValue = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(fetchedValue).isPresent();
    }

    public <T> Optional<T> fetch(@NonNull final String key, @NonNull final Class<T> targetClass) {
        final var value = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        if (value.isEmpty()) {
            return Optional.empty();
        }
        T result = objectMapper.convertValue(value.get(), targetClass);
        return Optional.of(result);
    }
}
