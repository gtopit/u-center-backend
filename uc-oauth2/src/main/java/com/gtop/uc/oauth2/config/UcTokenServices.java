package com.gtop.uc.oauth2.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * @author hongzw
 * 处理并发问题
 * 1、继承DefaultTokenService 在createAccessToken方法加上sychronized关键字。（分布式部署采用分布式锁处理）
 * 2、继承DefaultTokenService 将createAccessToken事务级别调SERIALIZABLE。这种方案的弊端在于使用的底层数据库得支持事务（表容易产生死锁）
 */
public class UcTokenServices extends DefaultTokenServices {

    @Override
    public synchronized OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        return super.createAccessToken(authentication);
    }
}
