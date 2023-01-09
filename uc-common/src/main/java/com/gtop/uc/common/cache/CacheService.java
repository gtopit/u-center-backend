package com.gtop.uc.common.cache;

/**
 * @author hongzw@citycloud.com.cn
 * @Date 2022/11/19 9:29
 */
public interface CacheService {

    /**
     * 设置缓存信息
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 设置缓存信息
     * @param key
     * @param value
     * @param timeout 毫秒
     */
    void set(String key, Object value, long timeout);

    /**
     * 获取缓存信息
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 删除缓存信息
     * @param key
     */
    void remove(String key);

}
