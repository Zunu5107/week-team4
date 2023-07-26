package com.clone.team4.global.redis;

import com.clone.team4.domain.user.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationRedisService {
    private final RedisTemplate redisTemplate;
    private final ValueOperations<String, String> values;

    public AuthenticationRedisService(@Qualifier("authenticationRedis") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.values = redisTemplate.opsForValue();
    }

    public String getValues(String key) {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        return values.get(key);
    }

    public void setValues(String key, String value) {
        values.set(key, value);
    }

    public boolean existKey(String key) {
        return values.get(key) != null;
    }

    public boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public enum AuthenticationStringEnum{
        ACCESS_TOKEN(AuthenticationString.ACCESS),
        REFRESH_TOKEN(AuthenticationString.REFRESH);

        private final String authenticationString;

        AuthenticationStringEnum(String authenticationString) {
            this.authenticationString = authenticationString;
        }

        public String getAuthenticationString() {
            return this.authenticationString;
        }

        public static class AuthenticationString {
            public static final String ACCESS = "ACCESS_TOKEN";
            public static final String REFRESH = "REFRESH_TOKEN";
        }
    }
}
