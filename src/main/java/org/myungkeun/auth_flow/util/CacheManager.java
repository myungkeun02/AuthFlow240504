package org.myungkeun.auth_flow.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.myungkeun.auth_flow.entity.Blog;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
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

    public String getData(String key){//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
        ValueOperations<String, Object> valueOperations=redisTemplate.opsForValue();
        return (String) valueOperations.get(key);
    }

    public void saveAllBlog(@NonNull final String key, @NonNull final List<Blog> list, @NonNull final Duration timeToLive) {
        redisTemplate.opsForValue().set(key, list, timeToLive);
    }

    public List<Blog> getAllBlog(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (List<Blog>) valueOperations.get(key);
    }

    public void saveBlog(@NonNull final Long key, @NonNull final Blog blog, @NonNull final Duration timeToLive) {
        redisTemplate.opsForValue().set(String.valueOf(key), blog, timeToLive);
    }
    public Object getBlog(Long key){
        ValueOperations<String, Object> valueOperations=redisTemplate.opsForValue();
        return valueOperations.get(String.valueOf(key));
    }



    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }


    public boolean checkExistsValue(String value) {
        return !value.equals("false");
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
