package com.gtop.uc.admin.constant;

import com.gtop.uc.common.constant.GlobalPropertiesValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hongzw
 */
@Configuration
public class Oauth2Properties {

    /**
     * oauth2服务地址
     */
    public static String UC_OAUTH2_SERVICE_URL;

    /**
     * 获取access token地址
     */
    public static String UC_OAUTH2_SERVICE_ACCESS_TOKEN_URL;

    /**
     * 用户跳转获取access token地址
     */
    public static String UC_OAUTH2_SERVICE_USER_AUTHORIZATION_URL;

    /**
     * 获取用户信息地址
     */
    public static String UC_OAUTH2_SERVICE_USER_INFO_URL;

    /**
     * 用户登出的地址
     */
    public static String UC_OAUTH2_SERVICE_USER_LOGOUT_URL;

    /**
     * access token校验地址
     */
    public static String UC_OAUTH2_SERVICE_CHECK_TOKEN_URL;

    @Value("${uc.oauth2.service.url:" + Oauth2PropertiesValue.UcOauth2ServiceUrl.DEFAULT + "}")
    public void setUcOauth2ServiceUrl(String ucOauth2ServiceUrl) {
        UC_OAUTH2_SERVICE_URL = ucOauth2ServiceUrl;
    }

    @Value("${uc.oauth2.service.access-token-url:" + Oauth2PropertiesValue.UcOauth2ServiceAccessTokenUrl.DEFAULT + "}")
    public void setUcOauth2ServiceAccessTokenUrl(String ucOauth2ServiceAccessTokenUrl) {
        UC_OAUTH2_SERVICE_ACCESS_TOKEN_URL = ucOauth2ServiceAccessTokenUrl;
    }

    @Value("${uc.oauth2.service.user-authorization-url:" + Oauth2PropertiesValue.UcOauth2ServiceUserAuthorizationUrl.DEFAULT + "}")
    public void setUcOauth2ServiceUserAuthorizationUrl(String ucOauth2ServiceUserAuthorizationUrl) {
        UC_OAUTH2_SERVICE_USER_AUTHORIZATION_URL = ucOauth2ServiceUserAuthorizationUrl;
    }

    @Value("${uc.oauth2.service.user-info-url:" + Oauth2PropertiesValue.UcOauth2ServiceUserInfoUrl.DEFAULT + "}")
    public void setUcOauth2ServiceUserInfoUrl(String ucOauth2ServiceUserInfoUrl) {
        UC_OAUTH2_SERVICE_USER_INFO_URL = ucOauth2ServiceUserInfoUrl;
    }

    @Value("${uc.oauth2.service.user-logout-url:" + Oauth2PropertiesValue.UcOauth2ServiceUserLogoutUrl.DEFAULT + "}")
    public void setUcOauth2ServiceUserLogoutUrl(String ucOauth2ServiceUserLogoutUrl) {
        UC_OAUTH2_SERVICE_USER_LOGOUT_URL = ucOauth2ServiceUserLogoutUrl;
    }

    @Value("${uc.oauth2.service.check-token-url:" + Oauth2PropertiesValue.UcOauth2ServiceCheckTokenUrl.DEFAULT + "}")
    public void setUcOauth2ServiceCheckTokenUrl(String ucOauth2ServiceCheckTokenUrl) {
        UC_OAUTH2_SERVICE_CHECK_TOKEN_URL = ucOauth2ServiceCheckTokenUrl;
    }

}
