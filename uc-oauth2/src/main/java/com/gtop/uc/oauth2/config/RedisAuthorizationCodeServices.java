package com.gtop.uc.oauth2.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.util.Assert;

/**
 * 使用redis存储authorization_code
 * @author hongzw
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final String PREFIX = "gtop:oauth2:authorization_code:";

    private final RedisTemplate<String, Object> redisTemplate;

    private final JdkSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public RedisAuthorizationCodeServices(RedisTemplate redisTemplate) {
        Assert.notNull(redisTemplate, "redis connection factory required");
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        String key = PREFIX + code;
        redisTemplate.opsForValue().set(key, serializationStrategy.serialize(authentication));
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        OAuth2Authentication auth2Authentication = serializationStrategy.deserialize((byte[]) redisTemplate.opsForValue().get(PREFIX + code), OAuth2Authentication.class);
        redisTemplate.delete(PREFIX + code);
        return auth2Authentication;
    }
}