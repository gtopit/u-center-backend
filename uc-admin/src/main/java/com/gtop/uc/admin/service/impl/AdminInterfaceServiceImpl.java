package com.gtop.uc.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gtop.uc.admin.constant.AdminConstant;
import com.gtop.uc.admin.dao.AdminInterfaceMapper;
import com.gtop.uc.admin.dao.OauthClientDetailsMapper;
import com.gtop.uc.admin.service.IAdminInterfaceService;
import com.gtop.uc.common.entity.sys.OauthClientDetails;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author hongzw@
 */
@Service
@Slf4j
public class AdminInterfaceServiceImpl implements IAdminInterfaceService {

    private final RedisTemplate redisTemplate;

    @Resource
    private AdminInterfaceMapper adminInterfaceMapper;

    @Resource
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AdminInterfaceServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    }

    @Override
    public boolean check(String clientId, String requestUrl) {
        Set<String> set = (Set<String>) redisTemplate.opsForValue().get(AdminConstant.CLIENT_INTERFACE_KEY_NAMESPACE + clientId);
        if (set == null) {
            OauthClientDetails details = oauthClientDetailsMapper.selectById(clientId);
            set = buildClientCache(details);
        }

        boolean hasPermission = false;
        for (String url : set) {
            if (antPathMatcher.match(url, requestUrl)) {
                log.info("OAuth2动态权限过滤：匹配应用功能 -- {}", url);
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }

    @Override
    public void buildCacheByResource(String resourceId) {

    }

    private Set<String> buildClientCache(OauthClientDetails clientDetails) {
        Set<String> set = new HashSet<>();
        String resourceIds = clientDetails.getResourceIds();
        if (StrUtil.isNotBlank(resourceIds)) {
            List<String> resourceIdList = Arrays.asList(resourceIds.split(","));
            List<String> urlList = adminInterfaceMapper.selectInterfaceUrlBy(resourceIdList);
            set.addAll(urlList);
            // 添加应用权限缓存
            redisTemplate.opsForValue().set(AdminConstant.CLIENT_INTERFACE_KEY_NAMESPACE + clientDetails.getClientId(), set, 30, TimeUnit.MINUTES);
        }
        return set;
    }

}
