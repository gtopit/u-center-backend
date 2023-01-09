package com.gtop.uc.common.cache;

import cn.hutool.cache.impl.TimedCache;
import com.gtop.uc.common.constant.GlobalProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/19 9:48
 */
@Service
@ConditionalOnProperty(name = "CACHE_SERVICE", havingValue = "local", matchIfMissing = true)
public class LocalCacheServiceImpl implements CacheService {

    private TimedCache<String, Object> timedCache;

    @PostConstruct
    public void init() {
        timedCache = new TimedCache<>(GlobalProperties.CACHE_SERVICE_DEFAULT_TIMEOUT);
    }

    @Override
    public void set(String key, Object value) {
        timedCache.put(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        timedCache.put(key, value, timeout);
    }

    @Override
    public Object get(String key) {
        return timedCache.get(key);
    }

    @Override
    public void remove(String key) {
        timedCache.remove(key);
    }

}
