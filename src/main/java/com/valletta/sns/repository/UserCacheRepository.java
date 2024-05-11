package com.valletta.sns.repository;

import com.valletta.sns.model.dto.UserDto;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, UserDto> userRedisTemplate;
    private static final Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(UserDto user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}, {}", key, user);

        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<UserDto> getUser(String userName) {
        String key = getKey(userName);

        UserDto user = userRedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get data from Redis {}, {}", key, user);

        return Optional.ofNullable(user);
    }

    private String getKey(String userName) {
        return "USER:" + userName;
    }
}
