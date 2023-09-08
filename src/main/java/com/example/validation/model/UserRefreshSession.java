package com.example.validation.model;

import com.example.validation.dto.UserDto;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(timeToLive = 1000 * 60 * 60 * 24 * 7)
public class UserRefreshSession {
    @Id
    private String sessionId;
    private UserDto userDto;
}
