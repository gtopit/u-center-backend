package com.gtop.uc.common.cache;

import com.gtop.uc.common.constant.GlobalProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/19 9:49
 */
@Service
@ConditionalOnProperty(name = "CACHE_SERVICE", havingValue = "redis")
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, GlobalProperties.CACHE_SERVICE_DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

}
