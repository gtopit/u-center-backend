package com.gtop.uc.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hongzw
 */
@Configuration
public class GlobalProperties {

    /**
     * 首次登录是否必须修改密码
     */
    public static boolean FIRST_LOGIN_CHANGE_PASSWORD;

    /**
     * 密码校验规则
     */
    public static String PASSWORD_VALIDATE_REGEX;

    /**
     * 登录需要图片认证码校验
     */
    public static boolean NEED_CAPTCHA;

    /**
     * 登录失败锁定启用
     */
    public static boolean LOGIN_FAIL_LOCK;

    /**
     * 登录最多连续失败尝试次数，之后再次失败则锁定
     */
    public static int LOGIN_FAIL_MAX_TIMES;

    /**
     * 连续登录失败锁定时间（分钟）
     */
    public static int LOGIN_FAIL_LOCK_MINUTES;

    /**
     * 认证中心后端URL地址
     */
    public static String AUTHENTICATION_API_URL;

    /**
     * 认证中心前端登录URL地址
     */
    public static String AUTHENTICATION_LOGIN_URL;

    /**
     * 平台级站点key
     */
    public static String PLATFORM_SITE_KEY;

    /**
     * 平台级站点主页
     */
    public static String PLATFORM_SITE_DASHBOARD_URL;

    /**
     * token过期时间
     */
    public static int OAUTH2_ACCESS_TOKEN_TIMEOUT;

    /**
     * token刷新时间
     */
    public static int OAUTH2_REFRESH_TOKEN_TIMEOUT;

    /**
     * 业务消息推送消息队列类型
     */
    public static String BIZ_MQ_TYPE;

    /**
     * 缓存服务默认过期时间
     */
    public static long CACHE_SERVICE_DEFAULT_TIMEOUT;

    /**
     * 校验码过期时间
     */
    public static long CAPTCHA_TIMEOUT;

    @Value("${FIRST_LOGIN_CHANGE_PASSWORD:" + GlobalPropertiesValue.FirstLoginChangePassword.DEFAULT + "}")
    public void setFirstLoginChangePassword(boolean firstLoginChangePassword) {
        FIRST_LOGIN_CHANGE_PASSWORD = firstLoginChangePassword;
    }

    @Value("${PASSWORD_VALIDATE_REGEX:" + GlobalPropertiesValue.NewPasswordValidateRegex.DEFAULT + "}")
    public void setPasswordValidateRegex(String passwordValidateRegex) {
        PASSWORD_VALIDATE_REGEX = passwordValidateRegex;
    }

    @Value("${NEED_CAPTCHA:" + GlobalPropertiesValue.NeedCaptcha.DEFAULT + "}")
    public void setNeedCaptcha(boolean needCaptcha) {
        NEED_CAPTCHA = needCaptcha;
    }

    @Value("${LOGIN_FAIL_LOCK:" + GlobalPropertiesValue.LoginFailLock.DEFAULT + "}")
    public void setLoginFailLock(boolean loginFailLock) {
        LOGIN_FAIL_LOCK = loginFailLock;
    }

    @Value("${LOGIN_FAIL_MAX_TIMES:" + GlobalPropertiesValue.LoginFailMaxTimes.DEFAULT + "}")
    public void setLoginFailMaxTimes(int loginFailMaxTimes) {
        LOGIN_FAIL_MAX_TIMES = loginFailMaxTimes;
    }

    @Value("${LOGIN_FAIL_LOCK_MINUTES:" + GlobalPropertiesValue.LoginFailLockMinutes.DEFAULT + "}")
    public void setLoginFailLockMinutes(int loginFailLockMinutes) {
        LOGIN_FAIL_LOCK_MINUTES = loginFailLockMinutes;
    }

    @Value("${AUTHENTICATION_API_URL:" + GlobalPropertiesValue.AuthenticationApiUrl.DEFAULT + "}")
    public void setAuthenticationApiUrl(String authenticationApiUrl) {
        AUTHENTICATION_API_URL = authenticationApiUrl;
    }

    @Value("${AUTHENTICATION_LOGIN_URL:" + GlobalPropertiesValue.AuthenticationLoginUrl.DEFAULT + "}")
    public void setAuthenticationLoginUrl(String authenticationLoginUrl) {
        AUTHENTICATION_LOGIN_URL = authenticationLoginUrl;
    }

    @Value("${PLATFORM_SITE_KEY:" + GlobalPropertiesValue.PlatformSiteKey.DEFAULT + "}")
    public void setHubProjectId(String platformSiteKey) {
        PLATFORM_SITE_KEY = platformSiteKey;
    }

    @Value("${PLATFORM_SITE_DASHBOARD_URL:" + GlobalPropertiesValue.PlatformSiteDashboardUrl.DEFAULT + "}")
    public void setPlatformSiteDashboardUrl(String platformSiteDashboardUrl) {
        PLATFORM_SITE_DASHBOARD_URL = platformSiteDashboardUrl;
    }

    @Value("${OAUTH2_ACCESS_TOKEN_TIMEOUT:" + GlobalPropertiesValue.Oauth2AccessTokenTimeout.DEFAULT + "}")
    public void setOauth2AccessTokenTimeout(int oauth2AccessTokenTimeout) {
        OAUTH2_ACCESS_TOKEN_TIMEOUT = oauth2AccessTokenTimeout;
    }

    @Value("${OAUTH2_REFRESH_TOKEN_TIMEOUT:" + GlobalPropertiesValue.Oauth2RefreshTokenTimeout.DEFAULT + "}")
    public void setOauth2RefreshTokenTimeout(int oauth2RefreshTokenTimeout) {
        OAUTH2_REFRESH_TOKEN_TIMEOUT = oauth2RefreshTokenTimeout;
    }

    @Value("${BIZ_MQ_TYPE:" + GlobalPropertiesValue.BizMqType.DEFAULT + "}")
    public void setBizMqType(String bizMqType) {
        BIZ_MQ_TYPE = bizMqType;
    }

    @Value("${CACHE_SERVICE_DEFAULT_TIMEOUT:" + GlobalPropertiesValue.CacheServiceDefaultTimeout.DEFAULT + "}")
    public void setCacheServiceDefaultTimeout(long cacheServiceDefaultTimeout) {
        CACHE_SERVICE_DEFAULT_TIMEOUT = cacheServiceDefaultTimeout;
    }

    @Value("${CAPTCHA_TIMEOUT:" + GlobalPropertiesValue.CaptchaTimeout.DEFAULT + "}")
    public void setCaptchaTimeout(long captchaTimeout) {
        CAPTCHA_TIMEOUT = captchaTimeout;
    }

}
