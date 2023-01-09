package com.gtop.uc.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.gtop.uc.admin.feignclient.Oauth2ServiceFeign;
import com.gtop.uc.admin.service.ISiteSecurityService;
import com.gtop.uc.common.cache.CachePlatformSiteProperties;
import com.gtop.uc.common.constant.GlobalProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Iterator;

/**
 * @author hongzw
 */
@Slf4j
@Controller
public class SiteHomepageController {

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	private ISiteSecurityService siteSecurityService;

	@GetMapping("/getCryptPasswordEncoder")
	@ResponseBody
	public String getCryptPasswordEncoder(@RequestParam("clientId") String clientids) {
		String pass = passwordEncoder.encode(clientids);
		return pass;
	}

	/**
	 * OAuth2 授权码模式 Authorization Code 该接口用于接收认证服务器推送的授权码，此时该资源服务器作为User-Agent角色
	 * <p>
	 * 同时，又以Client角色，向认证中心请求验证 Authorization Code，最终获取到 Access Token
	 * state传参目前定义:第一个参数为回调地址，第二个参数为scope,，中间逗号给开默认第一个参数必传
	 * 
	 * @return
	 */
	@GetMapping("/getTokenByCode")
	public String getToken(String code, String state, HttpServletRequest request) {

		Assert.notNull(state, "必须传入state！");
		String[] params = state.split(",");
		Assert.state(params.length == 3, "state传入格式必须是：回调地址,站点ID,用户ID");
		return doGetToken(request, code, params);

	}

	private String doGetToken(HttpServletRequest request, String code, String[] params) {
		return siteSecurityService.getToken(request, code, params);
	}

}
