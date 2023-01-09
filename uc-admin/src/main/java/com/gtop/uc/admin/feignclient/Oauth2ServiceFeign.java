package com.gtop.uc.admin.feignclient;

import com.gtop.uc.admin.constant.Oauth2PropertiesValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hongzw
 */
@FeignClient(url = "${uc.oauth2.service.url:" + Oauth2PropertiesValue.UcOauth2ServiceUrl.DEFAULT + "}", value = "oauth2ServiceFeign")
public interface Oauth2ServiceFeign {

    /**
     * 获取token
     * @param authorization_code
     * @param code
     * @param clientId
     * @param clientSecret
     * @param redirectUrl
     * @return
     */
    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    DefaultOAuth2AccessToken getToken(
            @RequestParam("grant_type") String authorization_code,
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUrl);

}
