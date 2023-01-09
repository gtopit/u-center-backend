package com.gtop.uc.oauth2.config.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtop.uc.common.constant.GlobalProperties;
import com.gtop.uc.common.response.ApiResponse;
import com.gtop.uc.oauth2.config.UcRedisTokenStore;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zhangxu
 */
@Component
public class UcLogoutSuccessHandler implements LogoutSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate redisTemplate;

	@Resource
	private ApplicationContext applicationContext;

	private static final String STR_TRUE = "true";

	private static final String STR_JSON_RESPONSE = "json_response";

	/** 默认token与session id关联的key前缀 */
	public static final String DEFAULT_KEY_NAMESPACE = "gtop:oauth2:session:";

	/**
	 * 这里是最终给用户的消息
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		String message;

		Map<String, String> data = new HashMap<>(4);
		data.put("loginPage", GlobalProperties.AUTHENTICATION_LOGIN_URL);
		data.put("loginUrl", GlobalProperties.AUTHENTICATION_API_URL + "/login");
		ObjectMapper mapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(authentication)) {
			message = "请先登录";
		} else {
			message = "退出成功";
			String userId = ((User) authentication.getPrincipal()).getUsername();
			UcRedisTokenStore tokenStore = applicationContext.getBean(UcRedisTokenStore.class);
			Collection<OAuth2AccessToken> oAuth2AccessTokenList = tokenStore.findTokensByUserName(userId);
			Iterator<OAuth2AccessToken> oAuth2AccessToken = oAuth2AccessTokenList.iterator();
			while (oAuth2AccessToken.hasNext()) {
				OAuth2AccessToken token = oAuth2AccessToken.next();
				tokenStore.removeAccessToken(token);

				String pattern = DEFAULT_KEY_NAMESPACE + token.getValue() + ":*";
				Set<String> keys = redisTemplate.keys(pattern);
				for (String key : keys) {
					//解绑oauth2 access_token和sessionId绑定
					redisTemplate.delete(key);
				}
			}
		}

		// 返回JSON
		if (STR_TRUE.equals(request.getParameter(STR_JSON_RESPONSE))) {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(mapper.writeValueAsString(ApiResponse.successWithData(message)));
		} else {/* 返回登陆页面 */
			redirectStrategy.sendRedirect(request, response, GlobalProperties.AUTHENTICATION_LOGIN_URL);
		}

	}
}
